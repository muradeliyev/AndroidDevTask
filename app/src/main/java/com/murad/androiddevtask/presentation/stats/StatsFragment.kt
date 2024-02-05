package com.murad.androiddevtask.presentation.stats

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.murad.androiddevtask.R
import com.murad.androiddevtask.common.base.BaseFragment
import com.murad.androiddevtask.common.base.ViewModelOwnerFragment
import com.murad.androiddevtask.common.extensions.getColorPalette
import com.murad.androiddevtask.common.recycler_view.RecyclerViewAdapter
import com.murad.androiddevtask.data.FakeData
import com.murad.androiddevtask.databinding.FragmentStatsBinding
import com.murad.androiddevtask.databinding.ItemCategoryBinding
import com.murad.androiddevtask.model.Category
import com.murad.androiddevtask.presentation.statements.StatementsBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.Async
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * Created on 03 February 2024, 6:29 PM
 * @author Murad Eliyev
 */


@AndroidEntryPoint
class StatsFragment : BaseFragment<FragmentStatsBinding>(FragmentStatsBinding::inflate),
    ViewModelOwnerFragment<StatsViewModel> {

    override val viewModel by viewModels<StatsViewModel>()

    private val categoriesAdapter by lazy {
        RecyclerViewAdapter<Category, ItemCategoryBinding>(ItemCategoryBinding::inflate) { category, i ->
            imageViewIcon.setImageResource(category.icon)
            imageViewIcon.getColorPalette {
                circularProgress.setIndicatorColor(it.getDominantColor(Color.YELLOW))
            }

            textViewName.text = category.name
            textViewPercentage.text = getString(R.string.percent, category.percentage.toString())
            textViewAmount.text = getString(
                R.string.azn,
                category.amount.toString() // TODO: add formatting
            )

            circularProgress.setProgress(category.percentage, true)

            root.setOnClickListener {
                navigateToStatementBottomSheet(category)
            }
        }
    }

    override fun FragmentStatsBinding.setup() {
        recyclerViewCategories.adapter = categoriesAdapter

        setupAutocompleteEditTexts()
        setupPieChart()
    }

    override fun setupCollectors() {
        viewModel.categories.collectUiResource(
            onSuccess = { categories ->
                categoriesAdapter.submitList(categories)

                configurePieChartWithCategories(categories)
            },
            onLoadingChanged = {
                setLoading(it)
            }
        )
    }

    private fun setupAutocompleteEditTexts() = with(binding) {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        autoCompleteTextViewYear.setText("$year")
        autoCompleteTextViewYear.setAdapter(NoFilterArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, (year - 20..year).toList()))

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("LLLL", Locale.getDefault())
        val month = dateFormat.format(calendar.time)
        autoCompleteTextViewMonth.setText(month)
        autoCompleteTextViewMonth.setAdapter(NoFilterArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, FakeData.months))

        autoCompleteTextViewCard.setText(FakeData.cards.first())
        autoCompleteTextViewCard.setAdapter(NoFilterArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, FakeData.cards))
    }

    private fun setupPieChart() = with(binding.pieChart) {
        description = null
        isDrawHoleEnabled = true
        holeRadius = 80f

        legend.isEnabled = false

        animateY(2000)
        setDrawEntryLabels(false)
        setNoDataText("")
        setHoleColor(Color.TRANSPARENT)

        setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                (e?.data as? Category)?.let { category ->
                    with(binding) {
                        textViewPercentage.text = "${category.name} ${category.percentage}%"
                        textViewAmount.text = category.amount.toString()

                        textViewViewStatement.setOnClickListener {
                            navigateToStatementBottomSheet(category)
                        }
                    }
                }
            }

            override fun onNothingSelected() {
            }
        })

        invalidate()
    }

    private fun configurePieChartWithCategories(categories: List<Category>) = with(binding.pieChart) {
        val entries = categories.take(6)
            .map {
                PieEntry(
                    it.percentage.toFloat(),
                    it.name,
                    it
                )
            }

        lifecycleScope.launch {
            with(binding.pieChart) {
                data = PieData(
                    PieDataSet(entries, "").apply {
                        colors = awaitColorsList(categories)
                        valueTextSize = 0f
                        sliceSpace = 5f
                    }
                )

                withContext(Dispatchers.Main) {
                    invalidate()
                }
            }
        }

    }

    private fun navigateToStatementBottomSheet(category: Category) {
        findNavController().navigate(
            StatsFragmentDirections.actionStatsFragmentToStatementsBottomSheetDialogFragment(
                model = StatementsBottomSheetDialogFragment.UiModel(
                    serviceName = category.name,
                    iconRes = category.icon,
                    amount = category.amount,
                    percent = category.percentage
                )
            )
        )
    }

    private fun setLoading(loading: Boolean) = with(binding) {
        circularProgress.isVisible = loading
        groupContent.isVisible = !loading
    }

    private suspend fun awaitColorsList(categories: List<Category>): List<Int> {
        return categories.map {
            val drawable = ContextCompat.getDrawable(requireContext(), it.icon) ?: ColorDrawable(Color.BLUE) // default color for Pie chart is blue
            lifecycleScope.async {
                awaitColor(drawable.toBitmap())
            }
        }.awaitAll().filterNotNull()
    }

    private suspend fun awaitColor(bitmap: Bitmap) = suspendCoroutine { cont ->
        Palette.from(bitmap).generate {
            cont.resume(it?.getDominantColor(Color.BLUE))
        }
    }

}


private class NoFilterArrayAdapter<T>(
    context: Context,
    resourceId: Int,
    objects: List<T>
) : ArrayAdapter<T>(context, resourceId, objects) {
    override fun getFilter() = AllPassFilter
}


private object AllPassFilter : Filter() {

    override fun performFiltering(p0: CharSequence?): FilterResults {
        return FilterResults()
    }

    override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
    }

}

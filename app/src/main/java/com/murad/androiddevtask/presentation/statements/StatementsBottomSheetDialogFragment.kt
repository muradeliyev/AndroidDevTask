package com.murad.androiddevtask.presentation.statements

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.murad.androiddevtask.R
import com.murad.androiddevtask.common.base.ViewModelOwnerFragment
import com.murad.androiddevtask.common.collectResource
import com.murad.androiddevtask.common.extensions.getColorPalette
import com.murad.androiddevtask.common.recycler_view.RecyclerViewAdapter
import com.murad.androiddevtask.databinding.BottomSheetStatementsBinding
import com.murad.androiddevtask.databinding.ItemStatementBinding
import com.murad.androiddevtask.model.Statement
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize


@AndroidEntryPoint
class StatementsBottomSheetDialogFragment : BottomSheetDialogFragment(),
    ViewModelOwnerFragment<StatementsViewModel> {

    private val args by navArgs<StatementsBottomSheetDialogFragmentArgs>()

    private var _binding: BottomSheetStatementsBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "Binding is null."
        }

    override val viewModel by viewModels<StatementsViewModel>()

    private val statementsAdapter by lazy {
        RecyclerViewAdapter<Statement, ItemStatementBinding>(ItemStatementBinding::inflate) { statement, i ->
            imageViewIcon.setImageResource(statement.icon)
            imageViewIcon.getColorPalette {
                val color = it.getDominantColor(Color.TRANSPARENT)
                imageViewIcon.backgroundTintList = ColorStateList.valueOf(color).withAlpha(100)
            }

            textViewName.text = statement.name
            textViewAmount.text = getString(R.string.azn, statement.amount.toString())
            textViewTime.text = statement.time
        }
    }

    private fun BottomSheetStatementsBinding.setup() {
        with(args.model as UiModel) {
            imageViewIcon.setImageResource(iconRes)
            imageViewIcon.getColorPalette {
                imageViewRing.imageTintList = ColorStateList.valueOf(it.getDominantColor(Color.TRANSPARENT))
            }

            textViewCategoryName.text = serviceName
            textViewAmount.text = getString(R.string.azn, amount.toString())
            textViewPercentage.text = getString(R.string.txt_of_all, percent.toString())

            viewModel.selectCategoryByName(serviceName)
        }

        recyclerViewStatements.adapter = statementsAdapter

        makeFloatingWindow()
    }

    override fun setupCollectors() {
        lifecycleScope.launch {
            viewModel.statements.collectResource(
                onSuccess = {
                    statementsAdapter.submitList(it)
                }
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomSheetStatementsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.setup()
        setupCollectors()
    }

    private fun makeFloatingWindow() {
        val designBottomSheet = dialog?.window?.decorView?.findViewById<ViewGroup>(com.google.android.material.R.id.design_bottom_sheet)
        val firstChild = designBottomSheet?.children?.firstOrNull()
        dialog?.setOnShowListener {
            val transparent = ColorDrawable(Color.TRANSPARENT)
            designBottomSheet?.background = transparent
            firstChild?.background = transparent
        }
    }


    @Parcelize
    data class UiModel(
        val serviceName: String,
        @DrawableRes val iconRes: Int,
        val amount: Int,
        val percent: Int,
    ) : Parcelable

}

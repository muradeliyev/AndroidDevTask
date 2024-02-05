package com.murad.androiddevtask.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.murad.androiddevtask.common.Inflater
import com.murad.androiddevtask.common.Resource
import com.murad.androiddevtask.common.collectResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 * Created on 03 February 2024, 6:30 PM
 * @author Murad Eliyev
 */


abstract class BaseFragment<Binding : ViewBinding>(
    private val inflater: Inflater<Binding>
) : Fragment() {

    private var _binding: Binding? = null
    protected val binding
        get() = requireNotNull(_binding) {
            "Binding is null."
        }

    open fun Binding.setup() = Unit

    protected fun <T> Flow<Resource<T>>.collectUiResource(
        onSuccess: (data: T) -> Unit,
        onLoadingChanged: (loading: Boolean) -> Unit = {},
        onError: (message: String?) -> Unit = {}
    ) {
        lifecycleScope.launch {
            flowWithLifecycle(lifecycle).collectResource(
                onSuccess = onSuccess,
                onLoadingChanged = onLoadingChanged,
                onError = onError
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflater(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setup()

        (this as? ViewModelOwnerFragment<*>)?.setupCollectors()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
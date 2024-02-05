package com.murad.androiddevtask.common.base

import androidx.lifecycle.ViewModel


/**
 * Created on 04 February 2024, 1:20 PM
 * @author Murad Eliyev
 */


interface ViewModelOwnerFragment<VM : ViewModel> {

    val viewModel: VM

    fun setupCollectors()

}

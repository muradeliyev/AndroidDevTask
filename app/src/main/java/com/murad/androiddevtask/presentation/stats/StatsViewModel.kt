package com.murad.androiddevtask.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murad.androiddevtask.common.Resource
import com.murad.androiddevtask.common.asResource
import com.murad.androiddevtask.domain.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


/**
 * Created on 04 February 2024, 11:41 AM
 * @author Murad Eliyev
 */


@HiltViewModel
class StatsViewModel @Inject constructor(
    repository: StatsRepository
) : ViewModel() {

    val categories = repository.getCategories()
        .asResource()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), Resource.Loading)

}

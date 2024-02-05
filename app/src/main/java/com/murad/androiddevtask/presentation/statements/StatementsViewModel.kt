package com.murad.androiddevtask.presentation.statements

import androidx.compose.runtime.referentialEqualityPolicy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murad.androiddevtask.common.Resource
import com.murad.androiddevtask.common.asResource
import com.murad.androiddevtask.data.FakeData
import com.murad.androiddevtask.domain.StatementsRepository
import com.murad.androiddevtask.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created on 04 February 2024, 1:44 PM
 * @author Murad Eliyev
 */


@HiltViewModel
class StatementsViewModel @Inject constructor(
    private val repository: StatementsRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<Category?>(null)

    val statements = repository.getStatements()
        .combine(_selectedCategory) { statements, category ->
            statements.map {
                it.copy(icon = category?.icon ?: it.icon) // just for making fake data as if it fetched for this category
            }
        }
        .asResource()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), Resource.Loading)


    fun selectCategoryByName(name: String) {
        viewModelScope.launch {
            repository.getCategories().find { it.name == name }?.let { found ->
                _selectedCategory.update {
                    found
                }
            }
        }
    }

}

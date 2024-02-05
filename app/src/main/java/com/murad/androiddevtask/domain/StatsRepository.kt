package com.murad.androiddevtask.domain

import com.murad.androiddevtask.model.Category
import kotlinx.coroutines.flow.Flow


/**
 * Created on 04 February 2024, 11:42 AM
 * @author Murad Eliyev
 */


interface StatsRepository {

    fun getCategories(): Flow<List<Category>>

}
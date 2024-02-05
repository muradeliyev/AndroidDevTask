package com.murad.androiddevtask.domain

import com.murad.androiddevtask.model.Category
import com.murad.androiddevtask.model.Statement
import kotlinx.coroutines.flow.Flow


/**
 * Created on 04 February 2024, 11:18 PM
 * @author Murad Eliyev
 */


interface StatementsRepository {

    suspend fun getCategories(): List<Category>

    fun getStatements(): Flow<List<Statement>>

}

package com.murad.androiddevtask.data

import com.murad.androiddevtask.domain.StatementsRepository
import com.murad.androiddevtask.model.Category
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created on 04 February 2024, 11:18 PM
 * @author Murad Eliyev
 */


@Singleton
class StatementsRepositoryImpl @Inject constructor() : StatementsRepository {

    override suspend fun getCategories(): List<Category> {
        return FakeData.categories
    }

    override fun getStatements() = flow {
        delay(1000) // simulating api request
        emit(FakeData.statements)
    }

}

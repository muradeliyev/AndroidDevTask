package com.murad.androiddevtask.data

import com.murad.androiddevtask.domain.StatsRepository
import com.murad.androiddevtask.model.Category
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created on 04 February 2024, 11:43 AM
 * @author Murad Eliyev
 */



@Singleton
class StatsRepositoryImpl @Inject constructor() : StatsRepository {

    override fun getCategories() = flow {
        delay(1000) // imagine fetching data from remote server
        emit(FakeData.categories)
    }

}

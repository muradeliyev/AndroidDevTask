package com.murad.androiddevtask.di

import com.murad.androiddevtask.data.StatementsRepositoryImpl
import com.murad.androiddevtask.data.StatsRepositoryImpl
import com.murad.androiddevtask.domain.StatementsRepository
import com.murad.androiddevtask.domain.StatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Created on 04 February 2024, 1:13 PM
 * @author Murad Eliyev
 */


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindStatsRepository(impl: StatsRepositoryImpl): StatsRepository

    @Binds
    @Singleton
    fun bindStatementsRepository(impl: StatementsRepositoryImpl): StatementsRepository

}

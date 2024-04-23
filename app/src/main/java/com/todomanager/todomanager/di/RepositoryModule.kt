package com.todomanager.todomanager.di

import com.todomanager.todomanager.repository.local.LocalRepository
import com.todomanager.todomanager.repository.local.LocalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository
}
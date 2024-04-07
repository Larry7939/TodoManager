package com.todomanager.todomanager.di

import android.content.Context
import com.todomanager.todomanager.datasource.local.LocalDataSource
import com.todomanager.todomanager.repository.local.LocalRepository
import com.todomanager.todomanager.repository.local.LocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(@ApplicationContext context: Context, json: Json): LocalDataSource =
        LocalDataSource.getInstance(context, json)

    @Singleton
    @Provides
    fun bindLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository =
        localRepositoryImpl

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        isLenient = true
        prettyPrint = true
        ignoreUnknownKeys = true
    }
}
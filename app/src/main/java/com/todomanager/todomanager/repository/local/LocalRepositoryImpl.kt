package com.todomanager.todomanager.repository.local

import com.todomanager.todomanager.datasource.LocalDataSource
import com.todomanager.todomanager.dto.Profile
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val localDataSource: LocalDataSource): LocalRepository {
    override fun setIsRegistered(isRegistered: Boolean) {
        localDataSource.setIsRegistered(isRegistered)
    }

    override fun getIsRegistered(): Boolean {
        return localDataSource.getIsRegistered()
    }

    override fun setProfile(profile: Profile) {
        localDataSource.setProfile(profile)
    }

    override fun getProfile(): Profile {
        return localDataSource.getProfile()
    }
}
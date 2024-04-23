package com.todomanager.todomanager.repository.local

import com.todomanager.todomanager.datasource.local.LocalDataSource
import com.todomanager.todomanager.model.Profile
import com.todomanager.todomanager.model.Task
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val localDataSource: LocalDataSource) :
    LocalRepository {
    override suspend fun getTask(taskId: String?): Task? {
        return localDataSource.getTask(taskId)
    }

    override suspend fun editTask(task: Task?) {
        localDataSource.editTask(task)
    }

    override suspend fun addTask(task: Task) {
        localDataSource.addTask(task)
    }

    override suspend fun removeTask(taskId: String) {
        localDataSource.removeTask(taskId)
    }

    override suspend fun getTaskList(): List<Task> {
        return localDataSource.getTaskList()
    }

    override suspend fun findTaskByTaskDate(taskDate: String): List<Task> {
        return localDataSource.findTaskByTaskDate(taskDate)
    }

    override suspend fun setIsRegistered(isRegistered: Boolean) {
        localDataSource.setIsRegistered(isRegistered)
    }

    override suspend fun getIsRegistered(): Boolean {
        return localDataSource.getIsRegistered()
    }

    override suspend fun setProfile(profile: Profile) {
        localDataSource.setProfile(profile)
    }

    override suspend fun getProfile(): Profile {
        return localDataSource.getProfile()
    }
}
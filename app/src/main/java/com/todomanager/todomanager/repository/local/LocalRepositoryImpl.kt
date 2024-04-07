package com.todomanager.todomanager.repository.local

import com.todomanager.todomanager.datasource.local.LocalDataSource
import com.todomanager.todomanager.model.Profile
import com.todomanager.todomanager.model.Task
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val localDataSource: LocalDataSource) :
    LocalRepository {
    override fun getTask(taskId: String?): Task? {
        return localDataSource.getTask(taskId)
    }

    override fun editTask(task: Task?) {
        localDataSource.editTask(task)
    }

    override fun addTask(task: Task) {
        localDataSource.addTask(task)
    }

    override fun removeTask(taskId: String) {
        localDataSource.removeTask(taskId)
    }

    override fun getTaskList(): List<Task> {
        return localDataSource.getTaskList()
    }

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
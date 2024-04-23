package com.todomanager.todomanager.repository.local

import com.todomanager.todomanager.model.Profile
import com.todomanager.todomanager.model.Task

interface LocalRepository {
    suspend fun getTask(taskId: String?): Task?
    suspend fun editTask(task: Task?)
    suspend fun addTask(task: Task)
    suspend fun removeTask(taskId: String)
    suspend fun getTaskList(): List<Task>
    suspend fun findTaskByTaskDate(taskDate: String): List<Task>
    suspend fun setIsRegistered(isRegistered: Boolean)
    suspend fun getIsRegistered(): Boolean
    suspend fun setProfile(profile: Profile)
    suspend fun getProfile(): Profile
}
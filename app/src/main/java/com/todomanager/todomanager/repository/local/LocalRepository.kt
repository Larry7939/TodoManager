package com.todomanager.todomanager.repository.local

import com.todomanager.todomanager.model.Profile
import com.todomanager.todomanager.model.Task

interface LocalRepository {
    fun getTask(taskId: String?): Task?
    fun editTask(task: Task?)
    fun addTask(task: Task)
    fun removeTask(taskId: String)
    fun getTaskList(): List<Task>
    fun findTaskByTaskDate(taskDate: String): List<Task>
    fun setIsRegistered(isRegistered: Boolean)
    fun getIsRegistered(): Boolean
    fun setProfile(profile: Profile)
    fun getProfile(): Profile
}
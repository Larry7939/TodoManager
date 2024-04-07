package com.todomanager.todomanager.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.todomanager.todomanager.model.Profile
import com.todomanager.todomanager.model.Task
import com.todomanager.todomanager.util.devErrorLog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Singleton
class LocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
    private val json: Json
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(LOCAL_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getTask(taskId: String?): Task? {
        if (taskId == null) {
            return null
        }
        val jsonTask = prefs.getString(KEY_TASK, null) ?: emptyList<Task>().toString()
        return json.decodeFromString<List<Task>>(jsonTask).find { it.id == taskId }
    }

    fun addTask(task: Task?) {
        val jsonTask = prefs.getString(KEY_TASK, null) ?: emptyList<Task>().toString()
        val taskList = json.decodeFromString<MutableList<Task>>(jsonTask)
        if (task != null) {
            taskList.add(task)
        }
        prefs.edit { putString(KEY_TASK, json.encodeToString(taskList)) }
    }

    fun editTask(task: Task?) {
        removeTask(task?.id)
        addTask(task)
    }

    fun removeTask(taskId: String?) {
        val jsonTask = prefs.getString(KEY_TASK, null) ?: emptyList<Task>().toString()
        val taskList = json.decodeFromString<MutableList<Task>>(jsonTask)
        val task = taskList.find { it.id == taskId }
        taskList.remove(task)
        prefs.edit { putString(KEY_TASK, json.encodeToString(taskList)) }
    }

    fun getTaskList(): List<Task> {
        val jsonTodo = prefs.getString(KEY_TASK, null) ?: emptyList<Task>().toString()
        return try {
            json.decodeFromString<List<Task>>(jsonTodo)
        } catch (e: SerializationException) {
            e.message?.let {
                devErrorLog(it)
            }
            emptyList<Task>()
        } catch (e: IllegalArgumentException) {
            e.message?.let {
                devErrorLog(it)
            }
            emptyList<Task>()
        }
    }

    fun setProfile(profile: Profile) {
        return prefs.edit {
            putString(KEY_PROFILE, json.encodeToString(profile))
        }
    }

    fun getProfile(): Profile {
        val jsonProfile = prefs.getString(KEY_PROFILE, null) ?: return Profile()
        return try {
            json.decodeFromString<Profile>(jsonProfile)
        } catch (e: SerializationException) {
            e.message?.let {
                devErrorLog(it)
            }
            Profile()
        } catch (e: IllegalArgumentException) {
            e.message?.let {
                devErrorLog(it)
            }
            Profile()
        }
    }

    fun setIsRegistered(isRegistered: Boolean) {
        prefs.edit { putBoolean(KEY_IS_REGISTERED, isRegistered) }
    }

    fun getIsRegistered(): Boolean {
        return prefs.getBoolean(KEY_IS_REGISTERED, false)
    }

    companion object {
        private const val LOCAL_PREFS_NAME = "local_prefs"
        private const val KEY_PROFILE = "profile"
        private const val KEY_TASK = "task"
        private const val KEY_IS_REGISTERED = "is_registered"

        @Volatile
        private var instance: LocalDataSource? = null
        fun getInstance(context: Context, json: Json): LocalDataSource {
            return instance ?: synchronized(this) {
                LocalDataSource(context, json).also { instance = it }
            }
        }
    }
}
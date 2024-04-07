package com.todomanager.todomanager.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todomanager.todomanager.dto.Task
import com.todomanager.todomanager.repository.local.LocalRepository
import com.todomanager.todomanager.util.devErrorLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val localRepositoryImpl: LocalRepository) :
    ViewModel() {
    private var _taskState = MutableStateFlow<Task?>(null)
    val taskState: StateFlow<Task?>
        get() = _taskState

    private var _taskListState = MutableStateFlow<List<Task>>(emptyList<Task>())
    val taskListState: StateFlow<List<Task>>
        get() = _taskListState

    fun getTask(taskId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.getTask(taskId)
            }.onSuccess { task ->
                _taskState.update { task }
            }.onFailure { devErrorLog() }
        }
    }


    fun addTask(task: Task, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.addTask(task)
            }.onSuccess {
                loadTaskList()
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }.onFailure { devErrorLog() }
        }
    }

    fun editTask(task: Task?, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.editTask(task)
            }.onSuccess {
                loadTaskList()
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }.onFailure { devErrorLog() }
        }
    }

    fun removeTask(taskId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.removeTask(taskId)
            }.onSuccess {
                loadTaskList()
            }.onFailure { devErrorLog() }
        }
    }

    fun loadTaskList() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.getTaskList()
            }.onSuccess { taskList ->
                _taskListState.update { sortTaskList(taskList) }
            }.onFailure { devErrorLog() }
        }
    }

    private fun sortTaskList(taskList: List<Task>): List<Task> {
        val parseDate: (String) -> LocalDateTime = {
            val formatter = DateTimeFormatter.ofPattern("yyyy MM/dd h:mm a", Locale.ENGLISH)
            LocalDateTime.parse(it, formatter)
        }

        return taskList.sortedBy { task -> parseDate(task.taskDate.toStringForSorting()) }
    }
}
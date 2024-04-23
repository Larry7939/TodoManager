package com.todomanager.todomanager.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todomanager.todomanager.model.Task
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

    /**
     * 로컬에 저장된 Task 로드
     * */
    fun getTask(taskId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.getTask(taskId)
            }.onSuccess { task ->
                _taskState.update { task }
            }.onFailure { devErrorLog() }
        }
    }

    /**
     * Task 로컬 저장
     * */
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

    /**
     * 로컬에 저장된 Task 수정
     * */
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

    /**
     * 로컬에 저장된 Task 삭제(완료)
     * */
    fun removeTask(taskId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.removeTask(taskId)
            }.onSuccess {
                loadTaskList()
            }.onFailure { devErrorLog() }
        }
    }

    /**
     * 로컬에 저장된 TaskList 로드
     * */
    fun loadTaskList() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.getTaskList()
            }.onSuccess { taskList ->
                _taskListState.update { sortTaskList(taskList) }
            }.onFailure { devErrorLog() }
        }
    }

    /**
     * 근시일 순으로 task 정렬 및 taskList 상태 업데이트
     * */
    private fun sortTaskList(taskList: List<Task>): List<Task> {
        val parseDate: (String) -> LocalDateTime = {
            val formatter = DateTimeFormatter.ofPattern("yyyy MM/dd h:mm a", Locale.US)
            LocalDateTime.parse(it, formatter)
        }

        return taskList.sortedBy { task -> parseDate(task.taskDate.toStringForSorting()) }
    }
}
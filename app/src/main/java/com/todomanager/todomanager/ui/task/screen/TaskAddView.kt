package com.todomanager.todomanager.ui.task.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.todomanager.todomanager.R
import com.todomanager.todomanager.constant.Destination.TASK_ADD
import com.todomanager.todomanager.constant.Destination.TASK_MAIN
import com.todomanager.todomanager.model.Task
import com.todomanager.todomanager.model.TaskDate
import com.todomanager.todomanager.ui.button.CtaButton
import com.todomanager.todomanager.ui.dialog.PickerDialog
import com.todomanager.todomanager.ui.task.TaskViewModel
import com.todomanager.todomanager.ui.textfield.InputTextField
import com.todomanager.todomanager.ui.theme.G2
import com.todomanager.todomanager.ui.theme.Typography
import com.todomanager.todomanager.util.Utils

class TaskAddView {
    @Composable
    fun TaskAddScreen(navController: NavController, taskViewModel: TaskViewModel) {
        // Task Name TextField Focus 상태 관리 변수
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        var task by rememberSaveable { mutableStateOf("") }
        val taskLength by rememberUpdatedState(newValue = task.length)
        var taskDate by remember { mutableStateOf(TaskDate()) }
        var time by rememberSaveable { mutableStateOf("") }
        var isDatePickerDialogVisible by remember { mutableStateOf(false) }
        var isTimePickerDialogVisible by remember { mutableStateOf(false) }
        var isAddTaskEnable by remember { mutableStateOf(false) } // Add CTA 버튼 enabled 상태

        // Task명, Date가 모두 있어야 Task 추가 가능함.
        LaunchedEffect(taskLength, time) {
            isAddTaskEnable = taskLength > 0 && taskDate.isEmpty().not()
        }
        Surface(modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { removeInputNameFocus(keyboardController, focusManager) }) {
            Column {
                Spacer(modifier = Modifier.height(35.dp))
                Text(
                    modifier = Modifier.padding(top = 10.dp, start = 30.dp),
                    text = stringResource(id = R.string.add_task),
                    style = Typography.headlineLarge,
                    color = G2
                )
                Spacer(modifier = Modifier.height(30.dp))
                InputTextField().CustomOutlinedTextField(
                    hint = stringResource(id = R.string.input_task),
                    focusRequester = focusRequester,
                    focusManager = focusManager
                )
                { text ->
                    task = text
                }
                Spacer(modifier = Modifier.height(25.dp))
                InputTextField().TaskDateTextField(
                    hint = stringResource(id = R.string.input_date),
                    taskDate = taskDate
                ) {
                    removeInputNameFocus(keyboardController, focusManager)
                    isDatePickerDialogVisible = true
                }
                Spacer(modifier = Modifier.weight(1f))
                CtaButton().TextButton(
                    stringResource(id = R.string.cta_add),
                    isAddTaskEnable
                ) {
                    taskViewModel.addTask(
                        Task(
                            id = Utils.createTaskId(), // 랜덤 Task Id 부여
                            name = task,
                            taskDate = taskDate
                        )
                    ) {
                        navigateToTaskMain(navController) // addTask 성공 시, Task Main 뷰로 navigate
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
            if (isDatePickerDialogVisible) {
                PickerDialog().CustomDatePickerDialog(
                    pattern = "yyyy,MM/dd",
                    isFutureSelectable = true,
                    isPastSelectable = false,
                    onDateSelected = {
                        if (it.isNotEmpty()) {
                            taskDate.year = it.split(",")[0]
                            taskDate.monthDate = it.split(",")[1]
                            isTimePickerDialogVisible = true // DatePicker 날짜 선택 완료 시, TimePicker 노출
                        }
                    },
                    onDismiss = {
                        isDatePickerDialogVisible = false
                    })
            }
            if (isTimePickerDialogVisible) {
                PickerDialog().CustomTimePicker(onTimeSelected = {
                    time = it
                    taskDate.hourMinute = it
                    isTimePickerDialogVisible = false
                }, onDismiss = {
                    // Dismiss시, DatePicker에서 선택한 taskDate 초기화
                    taskDate = TaskDate()
                    time = ""
                    isTimePickerDialogVisible = false
                }).show()
            }
        }
    }

    private fun navigateToTaskMain(navController: NavController) {
        navController.navigate(TASK_MAIN) {
            popUpTo(TASK_ADD) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    /**
     * TextFiled 제외 다른 뷰 클릭 또는 키보드 완료 버튼 클릭 시 키보드 숨김 및 TextFieldFocus 해제
     */
    private fun removeInputNameFocus(
        keyboardController: SoftwareKeyboardController?,
        focusManager: FocusManager
    ) {
        keyboardController?.hide()
        focusManager.clearFocus()
    }
}
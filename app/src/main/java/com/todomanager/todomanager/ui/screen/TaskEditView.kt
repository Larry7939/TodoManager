package com.todomanager.todomanager.ui.screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.todomanager.todomanager.constant.Destination
import com.todomanager.todomanager.constant.NavArgKey
import com.todomanager.todomanager.dto.Task
import com.todomanager.todomanager.dto.TaskDate
import com.todomanager.todomanager.ui.button.CtaButton
import com.todomanager.todomanager.ui.dialog.PickerDialog
import com.todomanager.todomanager.ui.textfield.InputTextField
import com.todomanager.todomanager.ui.theme.G2
import com.todomanager.todomanager.ui.theme.Typography

class TaskEditView {
    @Composable
    fun TaskEditScreen(navController: NavController, taskViewModel: TaskViewModel) {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        var isEditTaskEnable by remember { mutableStateOf(false) }
        val task by taskViewModel.taskState.collectAsState()
        var taskId by remember { mutableStateOf<String?>(task?.id) }
        var taskName by remember { mutableStateOf(task?.name) }
        var taskDate by remember { mutableStateOf(task?.taskDate) }
        var time by rememberSaveable { mutableStateOf("") }
        var isDatePickerDialogVisible by remember { mutableStateOf(false) }
        var isTimePickerDialogVisible by remember { mutableStateOf(false) }
        taskId =
            navController.currentBackStackEntry?.arguments?.getString(NavArgKey.TASK_ID_EDIT_KEY)
        LaunchedEffect(Unit) {
            taskId?.let {
                taskViewModel.getTask(taskId)
            }
        }
        LaunchedEffect(task) {
            taskId = task?.id
            taskName = task?.name
            taskDate = task?.taskDate
        }
        LaunchedEffect(taskName, taskDate) {
            isEditTaskEnable = taskName?.isEmpty() == false && taskDate?.isEmpty()?.not() == true
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
                    text = stringResource(id = R.string.edit_task),
                    style = Typography.headlineLarge,
                    color = G2
                )
                Spacer(modifier = Modifier.height(30.dp))
                InputTextField().CustomOutlinedTextField(
                    hint = stringResource(id = R.string.input_task),
                    text = taskName?:"",
                    focusRequester = focusRequester,
                    focusManager = focusManager
                )
                { text ->
                    taskName = text
                }
                Spacer(modifier = Modifier.height(25.dp))
                InputTextField().TaskDateTextField(
                    hint = stringResource(id = R.string.input_date),
                    taskDate = taskDate?: TaskDate("","","")
                ) {
                    removeInputNameFocus(keyboardController, focusManager)
                    isDatePickerDialogVisible = true
                }
                Spacer(modifier = Modifier.weight(1f))
                CtaButton().TextButton(
                    stringResource(id = R.string.cta_edit),
                    isEditTaskEnable
                ) {
                    if (taskId != null && taskName != null && taskDate?.isEmpty()?.not() == true) {
                        taskViewModel.editTask(
                            Task(taskId!!, taskName!!, taskDate!!)
                        ) {
                            navigateToTaskMain(navController)
                        }
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
                            taskDate?.year = it.split(",")[0]
                            taskDate?.monthDate = it.split(",")[1]
                            isTimePickerDialogVisible = true
                        }
                    },
                    onDismiss = {
                        isDatePickerDialogVisible = false
                    })
            }
            if (isTimePickerDialogVisible) {
                PickerDialog().CustomTimePicker(onTimeSelected = {
                    time = it
                    taskDate?.hourMinute = it
                    isTimePickerDialogVisible = false
                }, onDismiss = {
                    taskDate = TaskDate()
                    time = ""
                    isTimePickerDialogVisible = false
                }).show()
            }
        }
    }

    private fun navigateToTaskMain(navController: NavController) {
        navController.navigate(Destination.TASK_MAIN) {
            popUpTo(Destination.TASK_ADD) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    private fun removeInputNameFocus(
        keyboardController: SoftwareKeyboardController?,
        focusManager: FocusManager
    ) {
        keyboardController?.hide()
        focusManager.clearFocus()
    }
}
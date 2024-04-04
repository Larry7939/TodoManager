package com.todomanager.todomanager.ui.screen

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.todomanager.todomanager.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTimePickerDialog {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DatePickerDialog(
        onDateSelected: (String) -> Unit,
        onDismiss: () -> Unit
    ) {
        val datePickerState = rememberDatePickerState()

        val selectedDate = datePickerState.selectedDateMillis?.let {
            convertMillisToDate(it)
        } ?: ""

        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = {
                    onDateSelected(selectedDate)
                    onDismiss()
                }

                ) {
                    Text(text = stringResource(id = R.string.OK))
                }
            },
            dismissButton = {
                Button(onClick = {
                    onDismiss()
                }) {
                    Text(text = stringResource(id = R.string.CANCEL))
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    private fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        return formatter.format(Date(millis))
    }
}
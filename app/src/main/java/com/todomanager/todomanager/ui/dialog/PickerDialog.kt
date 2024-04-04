package com.todomanager.todomanager.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.todomanager.todomanager.R
import com.todomanager.todomanager.ui.theme.B1
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PickerDialog {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomDatePickerDialog(
        isFutureSelectable: Boolean,
        onDateSelected: (String) -> Unit,
        onDismiss: () -> Unit
    ) {
        val datePickerState = if (!isFutureSelectable) {
            val selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
            rememberDatePickerState(selectableDates = selectableDates)
        } else {
            rememberDatePickerState()
        }

        val selectedDate = datePickerState.selectedDateMillis?.let {
            convertMillisToDate(it)
        } ?: ""

        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    colors = ButtonColors(B1,Color.White,B1,B1),
                    onClick = {
                    onDateSelected(selectedDate)
                    onDismiss()
                }) { Text(text = stringResource(id = R.string.ok)) }
            },
            dismissButton = {
                Button(
                    colors = ButtonColors(B1,Color.White,B1,B1),
                    onClick = {
                    onDismiss()
                }) { Text(text = stringResource(id = R.string.cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    private fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        return formatter.format(Date(millis))
    }
}
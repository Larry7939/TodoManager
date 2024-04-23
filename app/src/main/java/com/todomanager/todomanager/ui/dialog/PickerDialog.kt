package com.todomanager.todomanager.ui.dialog

import android.app.TimePickerDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.todomanager.todomanager.R
import com.todomanager.todomanager.ui.theme.B1
import com.todomanager.todomanager.util.Utils.convertMillisToDate
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar

class PickerDialog {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomDatePickerDialog(
        pattern: String,
        isFutureSelectable: Boolean,
        isPastSelectable: Boolean,
        onDateSelected: (String) -> Unit,
        onDismiss: () -> Unit
    ) {
        val datePickerState = if (!isFutureSelectable && isPastSelectable) {
            val selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
            rememberDatePickerState(selectableDates = selectableDates)
        } else if (isFutureSelectable && !isPastSelectable) {
            val selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)
                        .toEpochMilli()
                }
            }
            rememberDatePickerState(selectableDates = selectableDates)
        } else if (!isFutureSelectable && !isPastSelectable) {
            val selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis == System.currentTimeMillis()
                }
            }
            rememberDatePickerState(selectableDates = selectableDates)
        } else {
            rememberDatePickerState()
        }

        val selectedDate = datePickerState.selectedDateMillis?.let {
            convertMillisToDate(pattern, it)
        } ?: ""

        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    colors = ButtonColors(B1, Color.White, B1, B1),
                    onClick = {
                        onDateSelected(selectedDate)
                        onDismiss()
                    }) { Text(text = stringResource(id = R.string.ok)) }
            },
            dismissButton = {
                Button(
                    colors = ButtonColors(B1, Color.White, B1, B1),
                    onClick = {
                        onDismiss()
                    }) { Text(text = stringResource(id = R.string.cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomTimePicker(
        onTimeSelected: (String) -> Unit,
        onDismiss: () -> Unit
    ): TimePickerDialog {
        val calendar = remember { Calendar.getInstance() }
        val timeState = remember { mutableStateOf("") }
        val context = LocalContext.current
        val timePickerDialog = TimePickerDialog(
            context,
            { view, hourOfDay, minute ->
                val adjustedHour = if (hourOfDay > 12) {
                    hourOfDay - 12
                } else if (hourOfDay == 0) {
                    12
                } else {
                    hourOfDay
                }
                val adjustedMinute = String.format("%02d", minute)
                val amPm = if (hourOfDay < 12) {
                    "AM"
                } else {
                    "PM"
                }

                timeState.value = "${adjustedHour}:${adjustedMinute} $amPm"
                onTimeSelected(timeState.value)
            },
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            false
        )
        timePickerDialog.setOnCancelListener { onDismiss() }
        return timePickerDialog
    }
}
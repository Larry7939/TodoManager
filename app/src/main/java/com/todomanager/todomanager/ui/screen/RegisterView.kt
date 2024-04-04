package com.todomanager.todomanager.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.todomanager.todomanager.R
import com.todomanager.todomanager.ui.textfield.InputTextField
import com.todomanager.todomanager.ui.dialog.PickerDialog
import com.todomanager.todomanager.ui.theme.B1
import com.todomanager.todomanager.ui.theme.Typography

class RegisterView {

    @Composable
    fun RegisterScreen() {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        var nameTextLength by remember { mutableIntStateOf(0) }
        val keyboardController = LocalSoftwareKeyboardController.current
        var isDatePickerDialogVisible by remember { mutableStateOf(false) }
        var date by remember { mutableStateOf("") }
        Surface(modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { removeInputNameFocus(keyboardController, focusManager) }) {
            Column(
                modifier = Modifier.padding(top = 85.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage()
                Spacer(modifier = Modifier.height(80.dp))
                InputTextField().CustomOutlinedTextField(
                    hint = stringResource(id = R.string.input_name),
                    maxLength = INPUT_NAME_MAX_LENGTH,
                    focusRequester = focusRequester,
                    focusManager = focusManager
                ) { text ->
                    nameTextLength = text.length
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 5.dp, end = 40.dp),
                    text = "$nameTextLength / $INPUT_NAME_MAX_LENGTH",
                    style = Typography.displaySmall,
                    color = B1
                )
                Spacer(modifier = Modifier.height(15.dp))
                InputTextField().DateTextField(date) {
                    removeInputNameFocus(keyboardController, focusManager)
                    isDatePickerDialogVisible = true
                }
            }
            if (isDatePickerDialogVisible) {
                PickerDialog().CustomDatePickerDialog(
                    onDateSelected = { date = it },
                    onDismiss = { isDatePickerDialogVisible = false })
            }
        }
    }

    private fun removeInputNameFocus(
        keyboardController: SoftwareKeyboardController?,
        focusManager: FocusManager
    ) {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    @Composable
    fun ProfileImage() {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .size(150.dp)
                .clip(CircleShape)
                .background(B1)
        ) {
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_photo_profile),
                contentDescription = "photo_profile"
            )
        }
    }

    companion object {
        const val INPUT_NAME_MAX_LENGTH = 15
    }
}
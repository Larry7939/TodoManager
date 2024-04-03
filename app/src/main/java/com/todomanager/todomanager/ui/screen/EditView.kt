package com.todomanager.todomanager.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.todomanager.todomanager.ui.theme.B1
import com.todomanager.todomanager.ui.theme.G5
import com.todomanager.todomanager.ui.theme.TodoManagerTheme

class EditView {

    @Composable
    fun InputTextField(
        hint: String,
        maxLines: Int = 1,
        maxLength: Int = 50,
        focusRequester: FocusRequester,
        focusManager: FocusManager,
        onTextChanged: (String) -> Unit = {}
    ) {
        var textState by remember { mutableStateOf("") }
        var isFocused by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(horizontal = 20.dp)
                .border(width = 2.dp, B1, shape = RoundedCornerShape(30.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(modifier = Modifier
                .focusRequester(focusRequester)
                .padding(start = 30.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
                singleLine = maxLines == 1,
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
                maxLines = maxLines,
                cursorBrush = SolidColor(B1),
                value = textState,
                onValueChange = {
                    if (textState.length < maxLength || it.length < textState.length) {
                        textState = it
                        onTextChanged(textState)
                    }
                },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textState.isEmpty() && !isFocused) {
                            Text(color = G5, text = hint)
                        }
                        innerTextField()
                    }
                }
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewInputText() {
        TodoManagerTheme {

        }
    }
}
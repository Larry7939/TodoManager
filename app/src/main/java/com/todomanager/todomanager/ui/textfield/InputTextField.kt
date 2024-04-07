package com.todomanager.todomanager.ui.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.todomanager.todomanager.model.TaskDate
import com.todomanager.todomanager.ui.theme.B1
import com.todomanager.todomanager.ui.theme.G5
import com.todomanager.todomanager.ui.theme.TodoManagerTheme
import com.todomanager.todomanager.ui.theme.Typography

class InputTextField {

    @Composable
    fun CustomOutlinedTextField(
        hint: String,
        text: String = "",
        maxLines: Int = 1,
        maxLength: Int = 50,
        focusRequester: FocusRequester,
        focusManager: FocusManager,
        onTextChanged: (String) -> Unit = {}
    ) {
        var textState by rememberSaveable { mutableStateOf("") }
        var isFocused by remember { mutableStateOf(false) }
        LaunchedEffect(text) {
            textState = text
        }

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
                .padding(start = 30.dp, end = 10.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
                singleLine = maxLines == 1,
                textStyle = Typography.bodyLarge,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
                maxLines = maxLines,
                cursorBrush = SolidColor(B1),
                value = textState,
                onValueChange = { newText ->
                    if (textState.length < maxLength || newText.length < textState.length) {
                        textState = newText.filter { it.isEnglishLetter() } // 영문만 입력 가능하도록 필터링
                        onTextChanged(textState)
                    }
                },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textState.isEmpty() && !isFocused) {
                            Text(color = G5, text = hint, style = Typography.bodyLarge)
                        }
                        innerTextField()
                    }
                }
            )
        }
    }

    @Composable
    fun DateTextField(hint: String, date: String, onClick: () -> Unit) {
        val text = date.ifEmpty { hint }
        val textColor = if (date.isEmpty()) G5 else Color.Black

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(horizontal = 20.dp)
                .border(width = 2.dp, B1, shape = RoundedCornerShape(30.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { onClick() }
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .align(Alignment.CenterStart),
                text = text,
                color = textColor,
                style = Typography.bodyLarge
            )
        }
    }

    @Composable
    fun TaskDateTextField(hint: String, taskDate: TaskDate, onClick: () -> Unit) {
        val isHintVisible = taskDate.isEmpty()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(horizontal = 20.dp)
                .border(width = 2.dp, B1, shape = RoundedCornerShape(30.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { onClick() }
        ) {
            if (isHintVisible) {
                Text(
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .align(Alignment.CenterStart),
                    text = hint,
                    color = G5,
                    style = Typography.bodyLarge
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    modifier = Modifier,
                    text = taskDate.year,
                    style = Typography.bodyLarge
                )
                Text(text = taskDate.monthDate)
                Text(text = taskDate.hourMinute)
            }
        }
    }

    /**
     * 입력된 문자가 영문인지 여부를 확인하기 위한 함수
     * */
    private fun Char.isEnglishLetter(): Boolean {
        val unicodeBlock = Character.UnicodeBlock.of(this)
        return (unicodeBlock == Character.UnicodeBlock.BASIC_LATIN)
    }


    @Preview(showBackground = true)
    @Composable
    fun PreviewInputText() {
        TodoManagerTheme {

        }
    }
}
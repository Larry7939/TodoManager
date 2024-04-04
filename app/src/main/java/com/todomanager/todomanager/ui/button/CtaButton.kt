package com.todomanager.todomanager.ui.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.todomanager.todomanager.ui.theme.B1
import com.todomanager.todomanager.ui.theme.G5
import com.todomanager.todomanager.ui.theme.Typography

class CtaButton {
    @Composable
    fun TextButton(text: String, isActivated: Boolean, onClick: () -> Unit) {
        val buttonBackgroundColor = if (isActivated) B1 else G5
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(horizontal = 20.dp)
                .background(color = buttonBackgroundColor, shape = RoundedCornerShape(30.dp))
                .clickable(enabled = isActivated,
                    indication = rememberRipple(radius = 20.dp),
                    interactionSource = remember { MutableInteractionSource() }) { onClick() }
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = text,
                color = Color.White,
                style = Typography.bodyLarge
            )
        }
    }
}
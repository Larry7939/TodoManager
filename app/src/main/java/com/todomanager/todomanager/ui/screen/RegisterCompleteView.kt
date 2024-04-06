package com.todomanager.todomanager.ui.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.todomanager.todomanager.R
import com.todomanager.todomanager.constant.Destination
import com.todomanager.todomanager.ui.button.CtaButton
import com.todomanager.todomanager.ui.theme.G2
import com.todomanager.todomanager.ui.theme.Typography

class RegisterCompleteView {
    @Composable
    fun RegisterCompleteScreen(
        navController: NavHostController,
        registerViewModel: RegisterViewModel
    ) {
        registerViewModel.getProfile()
        val profile by registerViewModel.profileState.collectAsState()

        Surface(modifier = Modifier.fillMaxSize()) {
            val painter = rememberAsyncImagePainter(profile.uri)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(85.dp))
                Image(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .graphicsLayer {
                            scaleX = -1f
                        },
                    painter = painter,
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                        setToSaturation(
                            0f
                        )
                    }),
                    contentScale = ContentScale.Crop,
                    contentDescription = "photo_profile"
                )
                Spacer(modifier = Modifier.height(85.dp))
                Text(text = profile.name, style = Typography.bodyLarge, color = G2)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = profile.birthday, style = Typography.displayLarge, color = G2)
                Spacer(modifier = Modifier.weight(1f))
                CtaButton().TextButton(
                    isActivated = true,
                    text = stringResource(id = R.string.cta_start)
                ) {
                    navController.navigate(Destination.TASK_MAIN) {
                        popUpTo(Destination.REGISTER) {
                            inclusive = true
                        }
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
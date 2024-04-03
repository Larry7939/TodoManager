package com.todomanager.todomanager.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.todomanager.todomanager.R
import com.todomanager.todomanager.constant.Destination
import com.todomanager.todomanager.ui.theme.O1
import com.todomanager.todomanager.ui.theme.TodoManagerTheme
import com.todomanager.todomanager.ui.theme.Typography
import kotlinx.coroutines.delay

class SplashView {
    @Composable
    fun SplashScreen(navController: NavController) {
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("splash.json"))
        val progress by animateLottieCompositionAsState(composition)

        LaunchedEffect(progress) {
            if (progress == 1f) {
                delay(SPLASH_DELAY)
                // SharedPreference에서 등록 여부 참조하여 사용자 등록 뷰 or 투두리스트 뷰로 이동
                navController.navigate(Destination.REGISTER)
            }
        }

        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.wrapContentSize()) {
                    LottieAnimation(
                        modifier = Modifier.size(150.dp),
                        composition = composition,
                        progress = { progress },
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = Typography.titleLarge,
                    color = O1
                )
            }
        }
    }

    companion object {
        const val SPLASH_DELAY = 1000L
    }

    @Composable
    @Preview
    fun SplashScreenPreview() {
        val navController = rememberNavController()
        TodoManagerTheme {
            SplashScreen(navController)
        }
    }
}
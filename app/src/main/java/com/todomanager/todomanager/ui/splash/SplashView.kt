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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.todomanager.todomanager.R
import com.todomanager.todomanager.constant.Destination
import com.todomanager.todomanager.constant.IOState
import com.todomanager.todomanager.ui.register.RegisterViewModel
import com.todomanager.todomanager.ui.theme.O1
import com.todomanager.todomanager.ui.theme.TodoManagerTheme
import com.todomanager.todomanager.ui.theme.Typography
import kotlinx.coroutines.delay

class SplashView {

    /**
     * 앱 진입 시, 사용자 등록 여부를 확인한 후, 사용자 등록 뷰 또는 TASK MAIN 뷰로 이동
     * */
    private fun navigateAfterSplash(navController: NavController, isRegistered: Boolean) {
        if (isRegistered) {
            navController.navigate(Destination.TASK_MAIN) {
                popUpTo(Destination.SPLASH) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Destination.REGISTER) {
                popUpTo(Destination.SPLASH) {
                    inclusive = true
                }
            }
        }
    }

    /**
     * 앱 진입 스플래시 뷰
     * */
    @Composable
    fun SplashScreen(navController: NavController, registerViewModel: RegisterViewModel) {
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("splash.json"))
        val progress by animateLottieCompositionAsState(composition)
        val getIsRegistered by registerViewModel.getRegisteredState.collectAsState()
        LaunchedEffect(progress) {
            if (progress == 1f) {
                delay(SPLASH_DELAY)
                registerViewModel.getIsRegistered()
            }
        }

        LaunchedEffect(getIsRegistered) {
            if (getIsRegistered == IOState.SUCCESS) {
                navigateAfterSplash(navController, registerViewModel.isRegisteredState.value)
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
        val registerViewModel: RegisterViewModel = hiltViewModel()
        TodoManagerTheme {
            SplashScreen(navController, registerViewModel)
        }
    }
}
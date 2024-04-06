package com.todomanager.todomanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.todomanager.todomanager.constant.NavArgKey.PROFILE_IMAGE_KEY
import com.todomanager.todomanager.constant.Destination
import com.todomanager.todomanager.ui.screen.CameraView
import com.todomanager.todomanager.ui.screen.RegisterCompleteView
import com.todomanager.todomanager.ui.screen.RegisterView
import com.todomanager.todomanager.ui.screen.RegisterViewModel
import com.todomanager.todomanager.ui.screen.SplashView
import com.todomanager.todomanager.ui.screen.TaskMainView
import com.todomanager.todomanager.ui.theme.TodoManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            val navController = rememberNavController()
            TodoManagerTheme {
                NavHost(navController = navController, startDestination = Destination.SPLASH) {

                    composable(Destination.SPLASH) {
                        SplashView().SplashScreen(navController, registerViewModel)
                    }
                    composable(route = Destination.REGISTER_WITH_ARG,
                        arguments = listOf(
                            navArgument(PROFILE_IMAGE_KEY) {
                                type = NavType.StringType
                                defaultValue = ""
                            }
                        )) {
                        RegisterView().RegisterScreen(navController, registerViewModel)
                    }
                    composable(route = Destination.CAMERA) {
                        CameraView().CameraScreen() { uri ->
                            navController.navigate("${Destination.REGISTER}?$PROFILE_IMAGE_KEY=$uri") {
                                launchSingleTop = true
                                popUpTo(Destination.CAMERA) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    composable(route = Destination.REGISTER_COMPLETE) {
                        RegisterCompleteView().RegisterCompleteScreen()
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        TodoManagerTheme {

        }
    }
}
package com.todomanager.todomanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.todomanager.todomanager.constant.Destination
import com.todomanager.todomanager.ui.screen.RegisterView
import com.todomanager.todomanager.ui.screen.SplashView
import com.todomanager.todomanager.ui.theme.TodoManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            TodoManagerTheme {
                NavHost(navController = navController, startDestination = Destination.SPLASH) {
                    composable(Destination.SPLASH) {
                        SplashView().SplashScreen(navController)
                    }
                    composable(Destination.REGISTER) {
                        RegisterView().RegisterScreen()
                    }
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
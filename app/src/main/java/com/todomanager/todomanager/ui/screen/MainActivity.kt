package com.todomanager.todomanager.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.todomanager.todomanager.constant.Destination
import com.todomanager.todomanager.ui.theme.TodoManagerTheme
import com.todomanager.todomanager.util.devTimberLog

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
                    composable(Destination.MAIN) {
                        MainView().MainScreen()
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
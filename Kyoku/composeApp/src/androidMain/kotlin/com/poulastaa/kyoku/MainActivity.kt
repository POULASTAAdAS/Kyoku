package com.poulastaa.kyoku

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.design_system.AppTheme
import com.poulastaa.common.ui.root.RootUiState
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
//    val splashScreen = installSplashScreen()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
        )
        setContent {
            var rootState by remember { mutableStateOf(RootUiState()) }

//            splashScreen.setKeepOnScreenCondition {
//                rootState.startDestination == Screens.Loading
//            }

            LaunchedEffect(Unit) {
                delay(800)
                rootState = rootState.copy(startDestination = Screens.AuthScreens.SignIn)
            }
            AppTheme {
                Surface {
                    App(state = rootState)
                }
            }
        }
    }
}
package com.poulastaa.kyoku

import android.app.Application
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.design_system.AppTheme
import com.poulastaa.common.ui.root.RootViewmodel
import com.poulastaa.kyoku.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class KyokuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@KyokuApp)
        }
    }
}

class MainActivity : ComponentActivity() {
    private val viewmodel by viewModel<RootViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewmodel.uiState.value.isLoading
        }

        splashScreen.setOnExitAnimationListener { splashProvider ->
            splashProvider.iconView.alpha = 0f

            splashProvider.view
                .animate()
                .alpha(0f)
                .setDuration(300L)
                .withEndAction { splashProvider.remove() }
                .start()
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
        )
        setContent {
            val rootState by viewmodel.uiState.collectAsStateWithLifecycle()

            if (rootState.startDestination != Screens.Loading) AppTheme {
                Surface {
                    RootNavigation(state = rootState)
                }
            }
        }
    }
}

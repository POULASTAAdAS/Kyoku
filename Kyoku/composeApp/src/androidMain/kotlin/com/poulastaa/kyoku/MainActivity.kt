package com.poulastaa.kyoku

//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.common.ui.design_system.AppTheme
import com.poulastaa.common.ui.root.RootViewmodel
import com.poulastaa.kyoku.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.compose.viewmodel.koinViewModel

class KyokuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@KyokuApp)
        }
    }
}

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
            val viewmodel = koinViewModel<RootViewmodel>()
            val rootState by viewmodel.state.collectAsStateWithLifecycle()

//            splashScreen.setKeepOnScreenCondition {
//                rootState.startDestination == Screens.Loading
//            }

            AppTheme {
                Surface {
                    RootNavigation(state = rootState)
                }
            }
        }
    }
}
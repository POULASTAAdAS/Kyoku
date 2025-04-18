package com.poulastaa.kyoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.KyokuThem
import com.poulastaa.kyoku.navigation.RootNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewmodel by viewModels<RootViewModel>()
    private val them by viewModels<ThemModeChanger.Companion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSplashScreen()
        enableEdgeToEdge()

        setContent {
            KyokuThem(
                darkTheme = them.themMode
            ) {
                val nav = rememberNavController()
                val state by viewmodel.state.collectAsStateWithLifecycle()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    state.screen?.let {
                        RootNavigation(
                            nav = nav,
                            screen = it,
                            toggleThem = {
                                them.toggleThemeMode()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setupSplashScreen() {
        var keepSplashOpened = true

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.keepSplashOn.collectLatest {
                    keepSplashOpened = it
                }
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }
    }
}
package com.poulastaa.kyoku

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.poulastaa.core.presentation.ui.KyokuThem
import com.poulastaa.kyoku.navigation.RootNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewmodel by viewModels<RootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSplashScreen()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.themeManager.isModeDark.collectLatest {
                    applyBarColors(it)
                }
            }
        }

        viewmodel.loadThem(
            isSystemThem = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    == Configuration.UI_MODE_NIGHT_YES
        )

        setContent {
            val mode by viewmodel.themeManager.isModeDark.collectAsStateWithLifecycle()
            val state by viewmodel.state.collectAsStateWithLifecycle()
            val themColor = viewmodel.themeManager.themColor

            KyokuThem(mode, themColor) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    state.screen?.let { screen ->
                        RootNavigation(
                            nav = rememberNavController(),
                            screens = screen
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

    private fun ComponentActivity.applyBarColors(isDark: Boolean) {
        enableEdgeToEdge(
            statusBarStyle = if (isDark) SystemBarStyle.dark(Color.Transparent.toArgb())
            else SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb()),
            navigationBarStyle = if (isDark) SystemBarStyle.dark(Color.Transparent.toArgb())
            else SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            window.isNavigationBarContrastEnforced = false
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }
    }
}
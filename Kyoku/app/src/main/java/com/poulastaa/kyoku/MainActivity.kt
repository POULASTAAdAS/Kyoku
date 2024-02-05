package com.poulastaa.kyoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.poulastaa.kyoku.data.model.LocalColorScheme
import com.poulastaa.kyoku.navigation.SetupNavGraph
import com.poulastaa.kyoku.presentation.screen.StartViewModel
import com.poulastaa.kyoku.ui.theme.DarkColorSchemeTypeTwo
import com.poulastaa.kyoku.ui.theme.KyokuTheme
import com.poulastaa.kyoku.ui.theme.LightColorSchemeTypeTwo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<StartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colorScheme = mutableStateOf(
            LocalColorScheme(
                light = LightColorSchemeTypeTwo,
                dark = DarkColorSchemeTypeTwo
            )
        )

        setUpSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            KyokuTheme(
                lightColor = colorScheme.value.light,
                darkColor = colorScheme.value.dark
            ) {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination by viewModel.startDestination.collectAsState()

                    startDestination?.let {
                        SetupNavGraph(
                            navController = navController,
                            startDestination = it,
                            changeThem = { localColorScheme ->
                                colorScheme.value = localColorScheme
                            }
                        )
                    }
                }
            }
        }
    }


    private fun setUpSplashScreen() {
        var keepSplashOpen = true

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.keepSplashOn.collect {
                    keepSplashOpen = it
                }
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpen
        }
    }
}


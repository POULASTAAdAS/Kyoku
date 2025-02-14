package com.poulastaa.main.presentation.home

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.core.presentation.designsystem.gradiantBackground
import com.poulastaa.core.presentation.ui.KyokuWindowSize

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun HomeRootScreen(
    viewmodel: HomeViewmodel,
    toggleDrawer: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = gradiantBackground()
                        )
                    )
                    .clickable {
                        toggleDrawer()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Home",
                    fontSize = MaterialTheme.typography.displayLarge.fontSize,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        mediumContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = gradiantBackground()
                        )
                    )
                    .clickable {
                        toggleDrawer()
                    },
            ) {
                Text(
                    text = "Home",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = MaterialTheme.typography.displayLarge.fontSize,
                    fontWeight = FontWeight.Black
                )
            }
        },
        expandedContent = {

        }
    )
}
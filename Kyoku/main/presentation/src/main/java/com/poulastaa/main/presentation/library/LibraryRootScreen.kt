package com.poulastaa.main.presentation.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.gradiantBackground

@Composable
internal fun LibraryRootScreen(
    viewmodel: LibraryViewmodel = hiltViewModel(),
    toggleDrawer: () -> Unit,
) {
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
            text = "Library",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            fontWeight = FontWeight.Black
        )
    }
}
package com.poulastaa.play.presentation.root_drawer.components.compact

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.HomeSelectedIcon
import com.poulastaa.core.presentation.designsystem.HomeUnSelectedIcon
import com.poulastaa.core.presentation.designsystem.LibrarySelectedIcon
import com.poulastaa.core.presentation.designsystem.LibraryUnSelectedIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun BoxScope.CompactBottomNavigation(
    currentDestination: String,
    saveScreen: SaveScreen,
    onSaveScreenToggle: (SaveScreen) -> Unit,
) {
    AnimatedVisibility(
        visible = (currentDestination == DrawerScreen.Home.route ||
                currentDestination == DrawerScreen.Library.route),
        modifier = Modifier.align(Alignment.BottomCenter),
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it }
    ) {
        val activity = LocalContext.current as Activity
        val screenWidth = calculateWindowSizeClass(activity = activity)


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 60.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .navigationBarsPadding()
                .padding(MaterialTheme.dimens.small1)
                .padding(top = MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.Bottom
        ) {
            Spacer(modifier = Modifier.weight(.5f))

            NavigationButton(
                modifier = if (screenWidth.widthSizeClass == WindowWidthSizeClass.Medium)
                    Modifier.size(56.dp) else Modifier,
                icon = if (saveScreen == SaveScreen.HOME) HomeSelectedIcon
                else HomeUnSelectedIcon,
                onClick = {
                    onSaveScreenToggle(SaveScreen.HOME)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            NavigationButton(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small1)
                    .then(
                        if (screenWidth.widthSizeClass == WindowWidthSizeClass.Medium)
                            Modifier.size(56.dp) else Modifier
                    ),
                icon = if (saveScreen == SaveScreen.LIBRARY) LibrarySelectedIcon
                else LibraryUnSelectedIcon,
                onClick = {
                    onSaveScreenToggle(SaveScreen.LIBRARY)
                }
            )

            Spacer(modifier = Modifier.weight(.5f))
        }
    }
}

@Composable
private fun NavigationButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.primary.copy(.8f)
        )
    ) {
        Icon(
            modifier = modifier
                .fillMaxSize(),
            imageVector = icon,
            contentDescription = null,
        )
    }
}
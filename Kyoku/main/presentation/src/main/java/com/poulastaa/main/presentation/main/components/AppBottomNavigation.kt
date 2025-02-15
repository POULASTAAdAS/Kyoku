package com.poulastaa.main.presentation.main.components

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.BOTTOM_BAR_HEIGHT
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.bottom_bar.Height
import com.poulastaa.core.presentation.designsystem.bottom_bar.Parabolic
import com.poulastaa.core.presentation.designsystem.ui.shapeCornerRadius
import com.poulastaa.core.presentation.ui.components.AnimatedNavigationBottomBar
import com.poulastaa.main.domain.model.AppNavigationBottomBarScreen

private const val ANIMATION_TIME = 500
private val ICON_SIZE = 30.dp

@Composable
internal fun AppBottomBar(
    modifier: Modifier = Modifier,
    screen: AppNavigationBottomBarScreen,
    onClick: (AppNavigationBottomBarScreen) -> Unit,
) {
    AnimatedNavigationBottomBar(
        modifier = modifier.height(BOTTOM_BAR_HEIGHT),
        selectedIndex = if (screen == AppNavigationBottomBarScreen.HOME) 0 else 1,
        ballColor = MaterialTheme.colorScheme.primary,
        barColor = MaterialTheme.colorScheme.background,
        cornerRadius = shapeCornerRadius(60.dp),
        ballAnimation = Parabolic(tween(ANIMATION_TIME, easing = LinearOutSlowInEasing)),
        indentAnimation = Height(
            indentWidth = 60.dp,
            indentHeight = 15.dp,
            animationSpec = tween(
                durationMillis = 700,
                easing = { OvershootInterpolator().getInterpolation(it) }
            )
        )
    ) {
        WiggleButton(
            modifier = Modifier.fillMaxSize(),
            iconSize = ICON_SIZE,
            isSelected = screen == AppNavigationBottomBarScreen.HOME,
            onClick = { onClick(AppNavigationBottomBarScreen.HOME) },
            icon = R.drawable.ic_home_empty,
            backgroundIcon = R.drawable.ic_home,
            enterExitAnimationSpec = tween(
                durationMillis = ANIMATION_TIME,
                easing = LinearEasing
            ),
        )

        WiggleButton(
            modifier = Modifier.fillMaxSize(),
            iconSize = ICON_SIZE,
            isSelected = screen == AppNavigationBottomBarScreen.LIBRARY,
            onClick = { onClick(AppNavigationBottomBarScreen.LIBRARY) },
            icon = R.drawable.ic_library_empty,
            backgroundIcon = R.drawable.ic_library,
            enterExitAnimationSpec = tween(
                durationMillis = ANIMATION_TIME,
                easing = LinearEasing
            ),
        )
    }
}
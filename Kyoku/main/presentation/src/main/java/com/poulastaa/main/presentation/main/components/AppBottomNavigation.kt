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
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.bottom_bar.Height
import com.poulastaa.core.presentation.designsystem.bottom_bar.Parabolic
import com.poulastaa.core.presentation.ui.components.AnimatedNavigationBottomBar
import com.poulastaa.core.presentation.designsystem.ui.shapeCornerRadius
import com.poulastaa.main.domain.model.AppBottomScreen

private const val ANIMATION_TIME = 500
private val BOTTOM_NAVIGATION_HEIGHT = 60.dp
private val ICON_SIZE = 30.dp

@Composable
internal fun AppBottomBar(
    modifier: Modifier = Modifier,
    screen: AppBottomScreen,
    onClick: (AppBottomScreen) -> Unit,
) {
    AnimatedNavigationBottomBar(
        modifier = modifier.height(BOTTOM_NAVIGATION_HEIGHT),
        selectedIndex = if (screen == AppBottomScreen.HOME) 0 else 1,
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
            isSelected = screen == AppBottomScreen.HOME,
            onClick = { onClick(AppBottomScreen.HOME) },
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
            isSelected = screen == AppBottomScreen.LIBRARY,
            onClick = { onClick(AppBottomScreen.LIBRARY) },
            icon = R.drawable.ic_library_empty,
            backgroundIcon = R.drawable.ic_library,
            enterExitAnimationSpec = tween(
                durationMillis = ANIMATION_TIME,
                easing = LinearEasing
            ),
        )
    }
}
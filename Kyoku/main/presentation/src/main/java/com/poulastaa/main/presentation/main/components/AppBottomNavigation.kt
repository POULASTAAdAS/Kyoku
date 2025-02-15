package com.poulastaa.main.presentation.main.components

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.main.domain.model.AppBottomScrren
import com.poulastaa.main.presentation.main.components.utils.shapeCornerRadius
import com.poulastaa.main.presentation.main.components.utils.Height
import com.poulastaa.mflix.core.presentation.designsystem.repository.Parabolic

@Stable
private const val Duration = 500

@Stable
private val wiggleButtonItems = listOf(
    WiggleButtonItem(
        icon = R.drawable.ic_home_empty,
        backgroundIcon = R.drawable.ic_home,
        isSelected = false,
        description = R.string.home_label
    ),
    WiggleButtonItem(
        icon = R.drawable.ic_library_empty,
        backgroundIcon = R.drawable.ic_library,
        isSelected = false,
        description = R.string.library_label
    )
)

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    screen: AppBottomScrren,
    onClick: (AppBottomScrren) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    AnimatedNavigationBottomBar(
        modifier = modifier
            .height(60.dp),
        selectedIndex = if (screen == AppBottomScrren.HOME) 0 else 1,
        ballColor = MaterialTheme.colorScheme.primary,
        barColor = MaterialTheme.colorScheme.tertiaryContainer,
        cornerRadius = shapeCornerRadius(60.dp),
        ballAnimation = Parabolic(tween(Duration, easing = LinearOutSlowInEasing)),
        indentAnimation = Height(
            indentWidth = 56.dp,
            indentHeight = 15.dp,
            animationSpec = tween(
                durationMillis = 700,
                easing = { OvershootInterpolator().getInterpolation(it) })
        )
    ) {
        WiggleButton(
            modifier = Modifier.fillMaxSize(),
            isSelected = screen == AppBottomScrren.HOME,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick(AppBottomScrren.HOME)
            },
            icon = wiggleButtonItems[0].icon,
            backgroundIcon = wiggleButtonItems[0].backgroundIcon,
            wiggleColor = MaterialTheme.colorScheme.error,
            outlineColor = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(id = wiggleButtonItems[0].description),
            enterExitAnimationSpec = tween(
                durationMillis = Duration,
                easing = LinearEasing
            ),
            iconSize = 30.dp,
            wiggleAnimationSpec = spring(dampingRatio = .45f, stiffness = 35f)
        )

        WiggleButton(
            modifier = Modifier.fillMaxSize(),
            isSelected = false,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            icon = wiggleButtonItems[1].icon,
            backgroundIcon = wiggleButtonItems[1].backgroundIcon,
            wiggleColor = MaterialTheme.colorScheme.error,
            outlineColor = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(id = wiggleButtonItems[1].description),
            enterExitAnimationSpec = tween(
                durationMillis = Duration,
                easing = LinearEasing
            ),
            iconSize = 30.dp,
            wiggleAnimationSpec = spring(dampingRatio = .45f, stiffness = 35f)
        )
    }
}
package com.poulastaa.core.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.ui.dimens
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private const val MAX_ANIMATION_TIME = 500

@Composable
fun StateFullKyokuWindowSize(
    sharedFlow: SharedFlow<SnackBarUiEvent>,
    windowSizeClass: WindowSizeClass,
    compactContent: @Composable () -> Unit,
    mediumContent: @Composable () -> Unit,
    expandedSmallContent: @Composable () -> Unit = {},
    expandedCompactContent: @Composable () -> Unit,
    expandedLargeContent: @Composable () -> Unit,
) {
    var currentMessage by remember { mutableStateOf<SnackBarUiEvent?>(null) }
    var isVisible by remember { mutableStateOf(false) }
    var currentJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()
    var isHideTriggered by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    ObserveAsEvent(sharedFlow) { message ->
        currentJob?.cancel() // Cancel previous auto-hide
        currentMessage = message
        isVisible = true

        currentJob = scope.launch {
            delay(5.seconds)
            isVisible = false
            delay(MAX_ANIMATION_TIME.toLong()) // Animation duration
            currentMessage = null
        }
    }

    fun hide() {
        if (isHideTriggered) return
        scope.launch {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            isHideTriggered = true
            currentJob?.cancel()
            isVisible = false
            delay(300)
            currentMessage = null
            isHideTriggered = false
        }
    }

    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> compactContent()
            WindowWidthSizeClass.Medium -> mediumContent()
            WindowWidthSizeClass.Expanded -> {
                when (windowSizeClass.heightSizeClass) {
                    WindowHeightSizeClass.Compact -> expandedCompactContent()
                    WindowHeightSizeClass.Medium -> expandedSmallContent()
                    WindowHeightSizeClass.Expanded -> expandedLargeContent()
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = isVisible && currentMessage != null,
                modifier = Modifier.fillMaxWidth(),
                enter = fadeIn(tween(MAX_ANIMATION_TIME))
                        + slideInVertically(tween(MAX_ANIMATION_TIME)),
                exit = fadeOut(tween(MAX_ANIMATION_TIME))
                        + slideOutVertically(tween(MAX_ANIMATION_TIME))
            ) {
                val message = currentMessage ?: return@AnimatedVisibility
                val color = when (message.eventType) {
                    SnackBarEventType.INFO -> MaterialTheme.colorScheme.primaryContainer
                    SnackBarEventType.SUCCESS -> MaterialTheme.colorScheme.primary
                    SnackBarEventType.ERROR -> MaterialTheme.colorScheme.errorContainer
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState {
                                if (it < 0) hide()
                            }
                        )
                        .systemGestureExclusion()
                        .background(color)
                        .padding(MaterialTheme.dimens.medium1)
                        .navigationBarsPadding()
                        .systemBarsPadding(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.message.asString(),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
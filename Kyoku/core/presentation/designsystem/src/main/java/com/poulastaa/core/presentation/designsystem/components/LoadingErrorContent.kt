package com.poulastaa.core.presentation.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.RetryIcon
import com.poulastaa.core.presentation.designsystem.ui.SadIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun LoadingErrorContent(retry: () -> Unit) {
    var retryAttempt by rememberSaveable { mutableIntStateOf(0) }
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimens.medium1),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = retryAttempt <= 3
        ) { state ->
            when (state) {
                true -> Icon(
                    imageVector = RetryIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .noRippleClickable {
                            retryAttempt++
                            retry()
                            haptic.performHapticFeedback(
                                HapticFeedbackType.LongPress
                            )
                        },
                    tint = MaterialTheme.colorScheme.primary
                )

                false -> Icon(
                    imageVector = SadIcon,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
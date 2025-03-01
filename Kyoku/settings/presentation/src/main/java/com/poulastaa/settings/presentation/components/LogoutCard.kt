package com.poulastaa.settings.presentation.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.LogoutIcon
import com.poulastaa.core.presentation.designsystem.ui.ShowMoreIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.settings.presentation.SettingsUiAction


@Composable
internal fun LogoutCard(
    modifier: Modifier,
    isLogoutBottomSheetVisible: Boolean,
    haptic: HapticFeedback,
    onAction: (SettingsUiAction.OpenLogoutDialog) -> Unit,
) {
    var dragScope by remember { mutableFloatStateOf(0f) }
    var cardMaxWidth by remember { mutableIntStateOf(0) }
    var sliderMaxWidth by remember { mutableIntStateOf(0) }

    LaunchedEffect(dragScope > 0 && isLogoutBottomSheetVisible) {
        dragScope = 0f
    }

    Card(
        modifier = modifier
            .onGloballyPositioned {
                cardMaxWidth = it.size.width
            },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .aspectRatio(1f)
                    .align(Alignment.CenterEnd)
                    .onGloballyPositioned {
                        sliderMaxWidth = it.size.width - 8 // removing padding
                    },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = LogoutIcon,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    Icon(
                        imageVector = ShowMoreIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(45f)
                            .aspectRatio(1f)
                            .padding(12.dp)
                    )
                }
            }

            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFCE9B99),
                    contentColor = MaterialTheme.colorScheme.errorContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .offset {
                        IntOffset(dragScope.toInt(), 0)
                    }
                    .draggable(
                        state = rememberDraggableState {
                            if (it > 0) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            dragScope = (it + dragScope).coerceAtLeast(0f)
                        },
                        orientation = Orientation.Horizontal,
                        onDragStopped = { _ ->
                            dragScope = if (dragScope < cardMaxWidth / 2) 0f
                            else {
                                onAction(SettingsUiAction.OpenLogoutDialog)
                                (cardMaxWidth - (sliderMaxWidth * 2)).toFloat()
                            }
                        }
                    )
                    .align(Alignment.CenterStart)
            ) {
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.logout_label),
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium3),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
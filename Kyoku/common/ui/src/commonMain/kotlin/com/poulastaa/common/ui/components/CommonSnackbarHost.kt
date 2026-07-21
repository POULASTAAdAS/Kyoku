package com.poulastaa.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poulastaa.common.ui.design_system.IconClose
import com.poulastaa.common.ui.design_system.StringDismissMessage
import com.poulastaa.common.ui.design_system.dimens
import com.poulastaa.common.ui.viewmodel.CommonUiStateHolder
import com.poulastaa.common.ui.viewmodel.SnackbarData
import com.poulastaa.common.ui.viewmodel.SnackbarType
import kotlinx.coroutines.delay

private const val SNACKBAR_DURATION_MILLIS = 5_000L
private val SNACKBAR_MAX_WIDTH = 600.dp

@Composable
fun CommonSnackbarHost(
    modifier: Modifier = Modifier,
) {
    val commonUiState by CommonUiStateHolder.uiState.collectAsState()
    val snackbarVisibility = remember { MutableTransitionState(false) }
    var displayedSnackbar by remember { mutableStateOf<SnackbarData?>(null) }

    LaunchedEffect(commonUiState.snackbarData) {
        val snackbarData = commonUiState.snackbarData

        if (snackbarData == null) snackbarVisibility.targetState = false
        else {
            displayedSnackbar = snackbarData
            snackbarVisibility.targetState = true
            delay(SNACKBAR_DURATION_MILLIS)
            snackbarVisibility.targetState = false
            CommonUiStateHolder.dismissSnackbar()
        }
    }

    LaunchedEffect(snackbarVisibility.currentState, snackbarVisibility.isIdle) {
        if (snackbarVisibility.isIdle && snackbarVisibility.currentState.not()) {
            displayedSnackbar = null
        }
    }

    AnimatedVisibility(
        visibleState = snackbarVisibility,
        modifier = modifier
            .navigationBarsPadding(),
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimens.layout.marginHorizontal,
                vertical = MaterialTheme.dimens.layout.marginVertical,
            ),
        ) {
            displayedSnackbar?.let { snackbarData ->
                CommonSnackbar(
                    data = snackbarData,
                    onDismiss = {
                        snackbarVisibility.targetState = false
                        CommonUiStateHolder.dismissSnackbar()
                    },
                )
            }
        }
    }
}

@Composable
private fun CommonSnackbar(
    data: SnackbarData,
    onDismiss: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .widthIn(max = SNACKBAR_MAX_WIDTH)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = when (data.type) {
            SnackbarType.Error -> MaterialTheme.colorScheme.errorContainer
            SnackbarType.Success -> MaterialTheme.colorScheme.primaryContainer
            SnackbarType.Info -> MaterialTheme.colorScheme.secondaryContainer
        },
        contentColor = when (data.type) {
            SnackbarType.Error -> MaterialTheme.colorScheme.onErrorContainer
            SnackbarType.Success -> MaterialTheme.colorScheme.onPrimaryContainer
            SnackbarType.Info -> MaterialTheme.colorScheme.onSecondaryContainer
        },
        shadowElevation = MaterialTheme.dimens.elevation.level3,
        tonalElevation = MaterialTheme.dimens.elevation.level3
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = MaterialTheme.dimens.spacing.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = data.message,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = MaterialTheme.dimens.spacing.medium),
                style = MaterialTheme.typography.bodyMedium,
            )

            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = IconClose,
                    contentDescription = StringDismissMessage,
                )
            }
        }
    }
}

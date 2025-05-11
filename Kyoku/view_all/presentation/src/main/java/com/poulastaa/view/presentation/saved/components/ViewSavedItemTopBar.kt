package com.poulastaa.view.presentation.saved.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.DeleteIcon
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar
import com.poulastaa.view.presentation.saved.ViewSavedUiAction
import com.poulastaa.view.presentation.saved.ViewSavedUiItemType

@Composable
internal fun ViewSavedItemTopBar(
    isEditEnabled: Boolean,
    type: ViewSavedUiItemType,
    navigateBack: () -> Unit,
    onAction: (ViewSavedUiAction) -> Unit,
) {
    AppBasicTopBar(
        title = "${stringResource(R.string.save)}d ${stringResource(type.value)}s",
        navigateBack = navigateBack,
        actions = {
            AnimatedVisibility(isEditEnabled) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                    ),
                    onClick = {
                        onAction(ViewSavedUiAction.OnDeleteAllToggleClick)
                    },
                    content = {
                        Icon(
                            imageVector = DeleteIcon,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewSavedItemTopBar(
                isEditEnabled = isSystemInDarkTheme(),
                type = ViewSavedUiItemType.ARTIST,
                navigateBack = {}) { }
        }
    }
}
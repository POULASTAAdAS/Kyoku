package com.poulastaa.view.presentation.saved.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.DeleteIcon
import com.poulastaa.core.presentation.designsystem.ui.EditIcon
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
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = if (isEditEnabled) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary,
                    containerColor = if (isEditEnabled) MaterialTheme.colorScheme.errorContainer
                    else Color.Transparent
                ),
                onClick = {
                    if (isEditEnabled) onAction(ViewSavedUiAction.OnDeleteAllClick)
                    else onAction(ViewSavedUiAction.OnEditToggle)
                },
                content = {
                    AnimatedContent(targetState = isEditEnabled) {
                        when (it) {
                            true -> Icon(
                                imageVector = DeleteIcon,
                                contentDescription = stringResource(R.string.delete)
                            )

                            false -> Icon(
                                imageVector = EditIcon,
                                contentDescription = stringResource(id = R.string.edit)
                            )
                        }
                    }
                }
            )
        }
    )
}
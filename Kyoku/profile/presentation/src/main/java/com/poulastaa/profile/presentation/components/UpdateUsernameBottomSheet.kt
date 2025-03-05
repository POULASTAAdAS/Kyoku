package com.poulastaa.profile.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.TextHolder
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppTextField
import com.poulastaa.profile.presentation.ProfileUiAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UpdateUsernameBottomSheet(
    username: TextHolder,
    isMakingApiCall: Boolean,
    onAction: (ProfileUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focus = LocalFocusManager.current

    ModalBottomSheet(
        onDismissRequest = {
            if (isMakingApiCall.not()) onAction(ProfileUiAction.OnEditUserNameToggle)
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        dragHandle = null,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = isMakingApiCall.not(),
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1)
                .padding(vertical = MaterialTheme.dimens.medium3)
        ) {
            AppTextField(
                text = username.value,
                onValueChange = { onAction(ProfileUiAction.OnUserNameChange(it)) },
                label = stringResource(R.string.username),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = UserIcon,
                isError = username.isErr,
                supportingText = username.errText.asString(),
                isClearButtonEnabled = true,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focus.clearFocus()
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAction(ProfileUiAction.OnConformUserName)
                    }
                ),
                onClearClick = { onAction(ProfileUiAction.OnUserNameChange("")) }
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small3))

            Row(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        onAction(ProfileUiAction.OnEditUserNameToggle)
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize(.8f),
                            imageVector = CloseIcon,
                            contentDescription = null
                        )
                    }
                }

                Card(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    onClick = {
                        focus.clearFocus()
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAction(ProfileUiAction.OnConformUserName)
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AnimatedContent(targetState = isMakingApiCall) {
                            when (it) {
                                true -> CircularProgressIndicator(
                                    modifier = Modifier.fillMaxSize(.6f),
                                    color = MaterialTheme.colorScheme.background,
                                    strokeWidth = 1.8.dp
                                )

                                false -> Icon(
                                    modifier = Modifier.fillMaxSize(.8f),
                                    imageVector = CheckIcon,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            UpdateUsernameBottomSheet(
                username = TextHolder(""),
                isMakingApiCall = false,
                onAction = {}
            )
        }
    }
}
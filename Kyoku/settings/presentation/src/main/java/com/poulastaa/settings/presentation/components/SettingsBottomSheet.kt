package com.poulastaa.settings.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.settings.presentation.SettingsUiAction


@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun SettingsBottomSheet(
    isLoading: Boolean,
    isLogoutBottomSheetVisible: Boolean,
    onAction: (SettingsUiAction) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = {
            if (isLoading.not()) onAction(
                if (isLogoutBottomSheetVisible) SettingsUiAction.CancelLogoutDialog
                else SettingsUiAction.CancelDeleteAccountDialog
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        dragHandle = null,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = isLoading.not(),
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (isLogoutBottomSheetVisible) Text(
                        text = "Are you sure you want to log out ?",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ) else Text(
                        text = "Are you sure you want to delete your account ?\n All your data will be lost.",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.large1))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                onAction(
                                    if (isLogoutBottomSheetVisible) SettingsUiAction.CancelLogoutDialog
                                    else SettingsUiAction.CancelDeleteAccountDialog
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.no).uppercase(),
                            )
                        }

                        Button(
                            onClick = {
                                onAction(
                                    if (isLogoutBottomSheetVisible) SettingsUiAction.OnLogoutDialog
                                    else SettingsUiAction.OnDeleteAccountDialog
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.yes).uppercase(),
                            )
                        }
                    }
                }

                CircularProgressIndicator(
                    modifier = Modifier
                        .alpha(if (isLoading) 1f else 0f)
                        .align(Alignment.Center)
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}
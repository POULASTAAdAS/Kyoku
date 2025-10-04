package com.poulastaa.auth.presentation.otp.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.ui.CheckIcon
import com.poulastaa.core.presentation.ui.CloseIcon
import com.poulastaa.core.presentation.ui.R

@Composable
internal fun FirstAlertDialog(
    modifier: Modifier = Modifier,
    onConform: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = onCancel,
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.primary,
        textContentColor = MaterialTheme.colorScheme.secondary,
        tonalElevation = 5.dp,
        title = {
            Text(text = stringResource(R.string.navigate_back_otp_screen_warning_title))
        },
        text = {
            Text(text = stringResource(R.string.navigate_back_otp_screen_warning_content))
        },
        confirmButton = {
            IconButton(
                onClick = onConform,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = CheckIcon,
                    contentDescription = stringResource(R.string.yes)
                )
            }
        },
        dismissButton = {
            IconButton(
                onClick = onCancel,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = stringResource(R.string.no)
                )
            }
        }
    )
}
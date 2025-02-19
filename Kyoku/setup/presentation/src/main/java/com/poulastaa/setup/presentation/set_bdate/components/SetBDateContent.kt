package com.poulastaa.setup.presentation.set_bdate.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.CalenderIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppLoadingButton
import com.poulastaa.setup.presentation.set_bdate.SetBDateUiAction
import com.poulastaa.setup.presentation.set_bdate.SetBDateUiState

@Composable
internal fun SetBDateContent(
    onAction: (SetBDateUiAction) -> Unit,
    haptic: HapticFeedback,
    state: SetBDateUiState,
) {
    Text(
        text = stringResource(R.string.bdate_title),
        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                onAction(SetBDateUiAction.OnDateDialogToggle)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
        value = state.date.value,
        isError = state.date.isErr,
        supportingText = {
            Text(
                text = state.date.errText.asString(),
            )
        },
        onValueChange = {},
        label = {
            Text(
                text = stringResource(R.string.bdate_label),
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = CalenderIcon,
                contentDescription = null
            )
        },
        enabled = false,
        shape = CircleShape,
        colors = TextFieldDefaults.colors(
            disabledLabelColor = if (state.date.isErr) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            disabledContainerColor = Color.Transparent,
            disabledSupportingTextColor = MaterialTheme.colorScheme.error,
            disabledTextColor = if (state.date.isErr) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            disabledLeadingIconColor = if (state.date.isErr) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            disabledIndicatorColor = if (state.date.isErr) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        )
    )

    Spacer(Modifier.height(MaterialTheme.dimens.large1))

    AppLoadingButton(
        modifier = Modifier
            .fillMaxWidth(.6f),
        text = stringResource(R.string.continue_text),
        isLoading = state.isMakingApiCall,
        onClick = {
            onAction(SetBDateUiAction.OnSubmitClick)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        fontWeight = FontWeight.SemiBold
    )
}
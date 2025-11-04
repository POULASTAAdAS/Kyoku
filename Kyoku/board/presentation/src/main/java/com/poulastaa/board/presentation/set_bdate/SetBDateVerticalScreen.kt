package com.poulastaa.board.presentation.set_bdate

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.poulastaa.board.presentation.R
import com.poulastaa.core.presentation.ConformButton
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.ArrowBackIcon
import com.poulastaa.core.presentation.ui.CalendarIcon
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait
import com.poulastaa.core.presentation.ui.dimens
import com.poulastaa.core.presentation.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SetBDateVerticalScreen(
    width: Float = 0.8f,
    modifier: Modifier = Modifier,
    state: SetUpBDateUiState,
    navigateBack: () -> Unit,
    onAction: (SetUpBDateUiAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { if (state.isMakingApiCall.not()) navigateBack() }
                    ) {
                        Icon(
                            imageVector = ArrowBackIcon,
                            contentDescription = stringResource(CoreR.string.back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.medium1)
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.medium1),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.set_bdate_title),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            OutlinedTextField(
                enabled = false,
                modifier = Modifier.fillMaxWidth(width),
                value = state.bDate.value,
                onValueChange = {},
                label = {
                    Text(
                        text = stringResource(R.string.bdate_label),
                        textAlign = TextAlign.Center
                    )
                },
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                ),
                leadingIcon = {
                    Icon(
                        imageVector = CalendarIcon,
                        contentDescription = null
                    )
                },
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = MaterialTheme.colorScheme.tertiary,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                    disabledLabelColor = MaterialTheme.colorScheme.tertiary,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.tertiary,
                    disabledIndicatorColor = MaterialTheme.colorScheme.tertiary,

                    errorTextColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorSupportingTextColor = MaterialTheme.colorScheme.error,
                    errorPlaceholderColor = MaterialTheme.colorScheme.error,
                    errorLeadingIconColor = MaterialTheme.colorScheme.error,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    selectionColors = TextSelectionColors(
                        handleColor = Color.Transparent,
                        backgroundColor = Color.Transparent
                    ),
                ),
                supportingText = {
                    Text(text = state.bDate.errText.asString())
                },
                isError = state.bDate.isErr
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small2))

            ConformButton(
                modifier = Modifier.fillMaxWidth((width / 2f) + 0.1f),
                isLoading = state.isMakingApiCall,
                onClick = {
                    onAction(SetUpBDateUiAction.OnSubmitClick)
                }
            )
        }
    }
}

@PreviewCompactPortrait
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        SetBDateVerticalScreen(
            state = SetUpBDateUiState(
                bDate = TextProp("10-10-2025")
            ),
            navigateBack = {},
            onAction = {}
        )
    }
}
package com.poulastaa.auth.presentation.email.forgot_password

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.EmailAlternateIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.components.AppLoadingButton
import com.poulastaa.core.presentation.ui.components.AppTextField
import com.poulastaa.core.presentation.designsystem.ui.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordMediumScreen(
    state: ForgotPasswordUiState,
    onAction: (ForgotPasswordUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    IconButton(
                        onClick = navigateBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = ArrowBackIcon,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .fillMaxHeight()
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimens.medium1)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AppTextField(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.email.value,
                    onValueChange = { onAction(ForgotPasswordUiAction.OnEmailChange(it)) },
                    label = stringResource(R.string.email),
                    leadingIcon = EmailAlternateIcon,
                    trailingIcon = if (state.email.isValid) CheckIcon else null,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onAction(ForgotPasswordUiAction.OnSubmitClick)
                        }
                    )
                )

                Spacer(Modifier.height(MaterialTheme.dimens.large2))

                AppLoadingButton(
                    modifier = Modifier.fillMaxWidth(.7f),
                    text = stringResource(R.string.continue_text),
                    onClick = { onAction(ForgotPasswordUiAction.OnSubmitClick) },
                    isLoading = state.isMakingApiCall,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }
}

@Preview(
    widthDp = 840,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 540
)
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ForgotPasswordMediumScreen(
                state = ForgotPasswordUiState(),
                onAction = {}
            ) {}
        }
    }
}
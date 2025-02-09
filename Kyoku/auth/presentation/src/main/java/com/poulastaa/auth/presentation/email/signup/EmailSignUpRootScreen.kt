package com.poulastaa.auth.presentation.email.signup

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.designsystem.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.KyokuWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@OptIn(
    ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun EmailSignUpRootScreen(
    viewModel: EmailSignUpViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToScreen: (SavedScreen) -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(context)
    val config = LocalConfiguration.current
    val haptic = LocalHapticFeedback.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val autoFill = LocalAutofill.current
    val autoFillPassword = AutofillNode(
        autofillTypes = listOf(AutofillType.Password),
        onFill = {
            viewModel.onAction(EmailSignUpUiAction.OnPasswordChange(it))
            viewModel.onAction(EmailSignUpUiAction.OnConformPasswordChange(it))
        }
    )
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {
            viewModel.onAction(EmailSignUpUiAction.OnEmailChange(it))
        }
    )

    val tree = LocalAutofillTree.current
    tree += autoFillEmail
    tree += autoFillPassword

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is EmailSignUpUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            EmailSignUpUiEvent.NavigateToLogIn -> navigateToLogin()

            is EmailSignUpUiEvent.OnSuccessNavigate -> {
                Log.d("called", "Called")
                navigateToScreen(event.screen)
            }
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            EmailSignUpCompactScreen(
                autoFill = autoFill,
                autoFillPassword = autoFillPassword,
                autoFillEmail = autoFillEmail,
                state = state,
                onAction = viewModel::onAction
            )
        },
        mediumContent = {
            EmailSignUpMediumScreen(
                autoFill = autoFill,
                autoFillPassword = autoFillPassword,
                autoFillEmail = autoFillEmail,
                state = state,
                onAction = viewModel::onAction
            )
        },
        expandedContent = {
            if (config.screenWidthDp > 980) EmailSignUpExpandedScreen(
                autoFill = autoFill,
                autoFillPassword = autoFillPassword,
                autoFillEmail = autoFillEmail,
                state = state,
                onAction = viewModel::onAction
            ) else EmailSignUpCompactExpandedScreen(
                autoFill = autoFill,
                autoFillPassword = autoFillPassword,
                autoFillEmail = autoFillEmail,
                state = state,
                onAction = viewModel::onAction
            )
        }
    )

    if (state.isPasskeyCreatePopUp) ModalBottomSheet(
        modifier = Modifier.padding(
            horizontal = if (config.screenWidthDp > 980) MaterialTheme.dimens.small1
            else MaterialTheme.dimens.medium1
        ),
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { false }
        ),
        shape = RoundedCornerShape(
            topStart = MaterialTheme.dimens.medium2,
            topEnd = MaterialTheme.dimens.medium2
        ),
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = {},
        onDismissRequest = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome),
                textDecoration = TextDecoration.Underline,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(MaterialTheme.dimens.large1))

            Text(
                text = stringResource(R.string.create_passkey),
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(MaterialTheme.dimens.large1))

            Button(
                modifier = Modifier.fillMaxWidth(.75f),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.onAction(EmailSignUpUiAction.OnCreatePasskey)
                },
                shape = MaterialTheme.shapes.small,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 0.dp
                ),
                content = {
                    Text(
                        text = stringResource(R.string.create).uppercase(),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 3.sp,
                        color = MaterialTheme.colorScheme.background,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        modifier = Modifier.padding(MaterialTheme.dimens.small3)
                    )

                    Spacer(Modifier.width(MaterialTheme.dimens.small2))

                    Icon(
                        imageVector = ArrowBackIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                            .rotate(180f),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            Button(
                modifier = Modifier.fillMaxWidth(.75f),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.onAction(EmailSignUpUiAction.OnCancelPasskeyCreation)
                },
                shape = MaterialTheme.shapes.small,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                content = {
                    Text(
                        text = stringResource(R.string.maybe_latter),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        modifier = Modifier.padding(MaterialTheme.dimens.small3)
                    )
                }
            )
        }
    }
}
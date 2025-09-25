package com.poulastaa.auth.presentation.intro

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.auth.presentation.intro.model.IntroUiState
import com.poulastaa.core.presentation.ui.AlternateEmailIcon
import com.poulastaa.core.presentation.ui.AppLogo
import com.poulastaa.core.presentation.ui.AppThem
import com.poulastaa.core.presentation.ui.CheckIcon
import com.poulastaa.core.presentation.ui.EyeCloseIcon
import com.poulastaa.core.presentation.ui.EyeOpenIcon
import com.poulastaa.core.presentation.ui.GoogleIcon
import com.poulastaa.core.presentation.ui.PasswordIcon
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun IntroHorizontalScreen(
    modifier: Modifier = Modifier,
    state: IntroUiState,
    onAction: (action: IntroUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focus = LocalFocusManager.current

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(MaterialTheme.dimens.medium1)
            .scrollable(
                state = rememberScrollState(0),
                orientation = Orientation.Horizontal
            )
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(.5f))

        Image(
            imageVector = AppLogo,
            contentDescription = stringResource(R.string.kyoku),
            modifier = Modifier.aspectRatio(1.7f)
        )

        Spacer(Modifier.weight(.3f))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.medium1),
            ) {
                Text(
                    text = stringResource(R.string.sing_in),
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                OutlinedTextField(
                    value = state.email.prop.value,
                    onValueChange = {
                        onAction(IntroUiAction.OnEmailChange(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        cursorColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,

                        focusedTextColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,

                        focusedSupportingTextColor = MaterialTheme.colorScheme.error,
                        unfocusedSupportingTextColor = MaterialTheme.colorScheme.error,

                        errorTextColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error,
                        errorCursorColor = MaterialTheme.colorScheme.error,
                        errorBorderColor = MaterialTheme.colorScheme.error
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.email)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = AlternateEmailIcon,
                            contentDescription = stringResource(R.string.email)
                        )
                    },
                    trailingIcon = state.email.isValidEmail.takeIf { it }?.let { _ ->
                        {
                            Icon(
                                imageVector = CheckIcon,
                                contentDescription = stringResource(R.string.email)
                            )
                        }
                    },
                    isError = state.email.prop.isErr,
                    supportingText = {
                        Text(
                            text = state.email.prop.errText.asString()
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focus.moveFocus(FocusDirection.Down)
                        }
                    )
                )

                Spacer(Modifier.height(MaterialTheme.dimens.small1))

                OutlinedTextField(
                    value = state.password.prop.value,
                    onValueChange = {
                        onAction(IntroUiAction.OnPasswordChange(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        cursorColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,

                        focusedTextColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,

                        focusedSupportingTextColor = MaterialTheme.colorScheme.error,
                        unfocusedSupportingTextColor = MaterialTheme.colorScheme.error,

                        errorTextColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error,
                        errorCursorColor = MaterialTheme.colorScheme.error,
                        errorBorderColor = MaterialTheme.colorScheme.error
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.password)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = PasswordIcon,
                            contentDescription = stringResource(R.string.password)
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onAction(IntroUiAction.ObPasswordVisibilityToggle)
                            }
                        ) {
                            AnimatedContent(
                                state.password.isPasswordVisible
                            ) {
                                when (it) {
                                    true -> Icon(
                                        imageVector = EyeOpenIcon,
                                        contentDescription = stringResource(R.string.password)
                                    )

                                    false -> Icon(
                                        imageVector = EyeCloseIcon,
                                        contentDescription = stringResource(R.string.password)
                                    )
                                }
                            }
                        }
                    },
                    isError = state.password.prop.isErr,
                    supportingText = {
                        Text(
                            text = state.password.prop.errText.asString()
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focus.clearFocus()
                            onAction(IntroUiAction.OnSubmit)
                        }
                    ),
                    visualTransformation = if (state.password.isPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.dimens.small1),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onAction(IntroUiAction.OnForgotPasswordClick)
                                }
                            ),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.forgot_password),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            AnimatedContent(
                state.isNewEmailUser
            ) {
                when (it) {
                    true -> Card(
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .animateContentSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.dimens.small3),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.email_not_registered),
                                fontWeight = FontWeight.Light,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = MaterialTheme.colorScheme.tertiary
                            )

                            Spacer(Modifier.height(MaterialTheme.dimens.small3))

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                shape = MaterialTheme.shapes.extraSmall,
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 5.dp
                                ),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                    onAction(IntroUiAction.OnEmailSingUpClick)
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(MaterialTheme.dimens.small1)
                                        .fillMaxWidth(.6f),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.create_new_account),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    false -> Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.dimens.small3),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dont_have_an_account),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        Spacer(Modifier.width(MaterialTheme.dimens.small2))

                        Text(
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                    onAction(IntroUiAction.OnEmailSingUpClick)
                                }
                            ),
                            text = stringResource(R.string.singup_button_text),
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(Modifier.weight(.5f))

        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                onAction(IntroUiAction.OnSubmit)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.fillMaxWidth(.6f),
            shape = MaterialTheme.shapes.extraSmall,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,
                pressedElevation = 0.dp
            )
        ) {
            Text(
                text = stringResource(R.string.continue_text),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Row(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                Modifier
                    .height(1.5.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .weight(1f)
            )

            Text(
                text = stringResource(R.string.or_login_with),
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small3)
            )

            Spacer(
                Modifier
                    .height(1.5.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .weight(1f)
            )
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Card(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .heightIn(max = 50.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = MaterialTheme.shapes.extraLarge,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp,
                pressedElevation = 0.dp
            ),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                onAction(IntroUiAction.OnGoogleAuthClick)
            }
        ) {
            AnimatedContent(
                state.isLoading
            ) {
                when (it) {
                    true -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.background,
                        )
                    }

                    false -> Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(3.dp)
                                .clip(CircleShape)
                                .fillMaxHeight()
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.tertiary)
                        ) {
                            Image(
                                painter = GoogleIcon,
                                contentDescription = stringResource(R.string.continue_with_google),
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = stringResource(R.string.continue_with_google),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = MaterialTheme.colorScheme.background
                        )

                        Spacer(Modifier.weight(1f))

                        Spacer(
                            modifier = Modifier
                                .padding(3.dp)
                                .clip(CircleShape)
                                .fillMaxHeight()
                                .aspectRatio(1f)
                                .alpha(0f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem(isSystemInDarkTheme()) {
        IntroHorizontalScreen(
            state = IntroUiState(),
            onAction = {}
        )
    }
}
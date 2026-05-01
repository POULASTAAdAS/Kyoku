package com.poulastaa.auth.ui.sign_in

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.components.RowSpacer
import com.poulastaa.common.ui.design_system.IconAppLogo
import com.poulastaa.common.ui.design_system.IconEmail
import com.poulastaa.common.ui.design_system.IconEyeClose
import com.poulastaa.common.ui.design_system.IconEyeOpen
import com.poulastaa.common.ui.design_system.IconGoogle
import com.poulastaa.common.ui.design_system.IconPasswordLock
import com.poulastaa.common.ui.design_system.IconShowMore
import com.poulastaa.common.ui.design_system.StringAppIcon
import com.poulastaa.common.ui.design_system.StringContinueWithGoogle
import com.poulastaa.common.ui.design_system.StringCreateAccount
import com.poulastaa.common.ui.design_system.StringDontHaveAccount
import com.poulastaa.common.ui.design_system.StringEmail
import com.poulastaa.common.ui.design_system.StringForgotPassword
import com.poulastaa.common.ui.design_system.StringInvalidEmail
import com.poulastaa.common.ui.design_system.StringOrSigninWith
import com.poulastaa.common.ui.design_system.StringPassword
import com.poulastaa.common.ui.design_system.StringPasswordVisibility
import com.poulastaa.common.ui.design_system.StringSignIn
import com.poulastaa.common.ui.design_system.StringSignInRest
import com.poulastaa.common.ui.design_system.StringSignInS
import com.poulastaa.common.ui.design_system.StringWelcomeBack
import com.poulastaa.common.ui.design_system.dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SignInScreen() {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val navController = LocalNavController.current

    // TODO: will be moved to viewmodel
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isError by remember { mutableStateOf(false) }
    val isPasswordVisible = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(MaterialTheme.dimens.layout.contentPadding)
            .navigationBarsPadding()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.3f)
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.small)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = IconAppLogo,
                contentDescription = StringAppIcon,
                modifier = Modifier.fillMaxSize(1f),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.weight(0.2f))

        Text(
            text = StringWelcomeBack,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        Text(
            text = buildAnnotatedString {
                withStyle(
                    MaterialTheme.typography.displayLarge.copy(fontStyle = FontStyle.Italic)
                        .toSpanStyle()
                ) {
                    append(StringSignInS)
                }
                withStyle(
                    MaterialTheme.typography.displaySmall.copy(fontStyle = FontStyle.Italic)
                        .toSpanStyle()
                ) {
                    append(StringSignInRest)
                }
            },
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        ColumnSpacer(MaterialTheme.dimens.spacing.hairline)

        Spacer(
            Modifier.width(MaterialTheme.typography.displayLarge.fontSize.value.dp)
                .clip(CircleShape)
                .height(MaterialTheme.dimens.spacing.hairline)
                .background(MaterialTheme.colorScheme.primary)
        )

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        AppOutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = StringEmail,
            leadingIcon = IconEmail,
            isError = isError,
            supportingText = if (isError) StringInvalidEmail else "",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )

        AppOutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = StringPassword,
            leadingIcon = IconPasswordLock,
            supportingText = "",
            trailingContent = {
                AnimatedContent(
                    targetState = isPasswordVisible.value,
                    modifier = Modifier.clip(CircleShape).clickable(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isPasswordVisible.value = !isPasswordVisible.value
                        }
                    )
                ) {
                    Icon(
                        imageVector = if (it) IconEyeClose else IconEyeOpen,
                        contentDescription = StringPasswordVisibility,
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus(false)
                }
            ),
            visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = StringForgotPassword,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clip(MaterialTheme.shapes.small).clickable(
                    onClick = {
                        navController.navigate(Screens.AuthScreens.ForgotPassword(email.value))
                    }
                ),
            )
        }

        Spacer(Modifier.weight(0.3f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.small)
        ) {
            ElevatedGradientButton(
                width = 0.7f,
                onClick = {
                    // TODO: action signin with email
                },
                colors = CardDefaults.elevatedCardColors(
                    contentColor = MaterialTheme.colorScheme.background,
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small,
                        ).minimumInteractiveComponentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = StringSignIn, fontWeight = FontWeight.SemiBold)

                    RowSpacer(MaterialTheme.dimens.spacing.small)

                    Icon(imageVector = IconShowMore, contentDescription = StringSignIn)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.extraSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    Modifier.height(1.5.dp).weight(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Text(
                    text = StringOrSigninWith,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    Modifier.height(1.5.dp).weight(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }

            ElevatedGradientButton(
                width = 1f,
                onClick = {
                    // TODO: action signin with google
                },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small,
                        )
                        .minimumInteractiveComponentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .padding(MaterialTheme.dimens.spacing.hairline)
                            .background(
                                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.small
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = IconGoogle,
                            contentDescription = StringContinueWithGoogle,
                            modifier = Modifier.minimumInteractiveComponentSize(),
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = StringContinueWithGoogle,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.background,
                    )

                    Spacer(Modifier.weight(1f))

                    // place holder
                    Box(
                        Modifier.background(
                            Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        ).alpha(0f)
                    ) {
                        Icon(imageVector = IconGoogle, contentDescription = null)
                    }
                }
            }
        }

        Spacer(Modifier.weight(0.1f))

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val annotatedString = buildAnnotatedString {
                withStyle(
                    MaterialTheme.typography.bodyMedium.toSpanStyle()
                        .copy(color = MaterialTheme.colorScheme.onSurface)
                ) {
                    append(StringDontHaveAccount)
                }

                withLink(
                    LinkAnnotation.Clickable(
                        tag = "CREATE_ACCOUNT",
                        styles = TextLinkStyles(
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline,
                            ).toSpanStyle()
                        ),
                        linkInteractionListener = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            navController.navigate(Screens.AuthScreens.SignUp)
                        }
                    )
                ) {
                    append(StringCreateAccount)
                }
            }

            Text(text = annotatedString)
        }

        ColumnSpacer(MaterialTheme.dimens.spacing.extraSmall)
    }
}

@Composable
private fun ElevatedGradientButton(
    width: Float = 0.6f,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(width),
            elevation = CardDefaults.cardElevation(
                defaultElevation = MaterialTheme.dimens.elevation.level3,
                pressedElevation = 0.dp,
            ),
            shape = MaterialTheme.shapes.small,
            colors = colors,
            onClick = onClick,
            content = content,
        )
    }
}
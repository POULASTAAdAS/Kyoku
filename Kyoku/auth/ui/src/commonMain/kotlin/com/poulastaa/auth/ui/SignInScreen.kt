package com.poulastaa.auth.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
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
import com.poulastaa.common.ui.IconEmail
import com.poulastaa.common.ui.IconEyeClose
import com.poulastaa.common.ui.IconEyeOpen
import com.poulastaa.common.ui.IconPasswordLock
import com.poulastaa.common.ui.IconShowMore
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.components.RowSpacer
import com.poulastaa.common.ui.dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SignInScreen() {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

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
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = IconEmail,
                contentDescription = "app icon",
                modifier = Modifier.fillMaxSize(0.8f)
            )
        }

        Spacer(Modifier.weight(0.2f))

        Text(
            text = "WELCOME BACK",
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
                    append("S")
                }
                withStyle(
                    MaterialTheme.typography.displaySmall.copy(fontStyle = FontStyle.Italic)
                        .toSpanStyle()
                ) {
                    append("ign in.")
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
            label = "email",
            leadingIcon = IconEmail,
            isError = isError,
            supportingText = if (isError) "Invalid email" else "",
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
            label = "password",
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
                        contentDescription = "password visibility",
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
                text = "Forgot password ?",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clip(MaterialTheme.shapes.small).clickable(
                    onClick = {
                        // TODO: navigate to forgot password screen
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
                onClick = {
                    // TODO: navigate to sign up screen
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.onBackground,
                                ),
                            ),
                            shape = MaterialTheme.shapes.small,
                        ).minimumInteractiveComponentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "SIGN IN", fontWeight = FontWeight.SemiBold)

                    RowSpacer(MaterialTheme.dimens.spacing.small)

                    Icon(imageVector = IconShowMore, contentDescription = "sign in")
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
                    text = "or signin with",
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
                    // TODO: navigate to sign up screen
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.onBackground,
                                ),
                            ),
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
                                MaterialTheme.colorScheme.onPrimaryContainer,
                                shape = MaterialTheme.shapes.small
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = IconShowMore,
                            contentDescription = "sign in",
                            modifier = Modifier.minimumInteractiveComponentSize()
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "CONTINUE WITH GOOGLE",
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))

                    // place holder
                    Box(
                        Modifier.background(
                            Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        ).alpha(0f)
                    ) {
                        Icon(imageVector = IconShowMore, contentDescription = null)
                    }
                }
            }
        }

        Spacer(Modifier.weight(0.1f))

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val annotatedString = buildAnnotatedString {
                withStyle(
                    MaterialTheme.typography.bodyMedium.toSpanStyle()
                ) {
                    append("Don't have an account ? ")
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
                            // TODO: navigate to create account screen
                        }
                    )
                ) {
                    append("Create Account")
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
            colors = CardDefaults.elevatedCardColors(
                contentColor = MaterialTheme.colorScheme.background,
            ),
            onClick = onClick,
            content = content,
        )
    }
}
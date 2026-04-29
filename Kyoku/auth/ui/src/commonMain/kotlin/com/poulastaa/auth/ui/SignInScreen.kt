package com.poulastaa.auth.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.common.ui.IconEmail
import com.poulastaa.common.ui.IconEyeClose
import com.poulastaa.common.ui.IconEyeOpen
import com.poulastaa.common.ui.IconPasswordLock
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SignInScreen() {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(MaterialTheme.dimens.layout.contentPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
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

        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val isError by remember { mutableStateOf(false) }
        val isPasswordVisible = remember { mutableStateOf(false) }

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
    }
}
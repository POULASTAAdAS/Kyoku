package com.poulastaa.core.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.EyeCloseIcon
import com.poulastaa.core.presentation.designsystem.ui.EyeOpenIcon
import com.poulastaa.core.presentation.designsystem.ui.PasswordIcon

@Composable
fun AppPasswordField(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    onPasswordToggleClick: () -> Unit,
    leadingIcon: ImageVector? = null,
    isPasswordVisible: Boolean = false,
    supportingText: String? = null,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Password,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier,
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = onPasswordToggleClick
            ) {
                AnimatedContent(
                    targetState = isPasswordVisible,
                    label = "password toggle animation",
                    transitionSpec = {
                        (fadeIn(tween(durationMillis = 220, delayMillis = 90)) +
                                slideInVertically(
                                    tween(
                                        durationMillis = 220,
                                        delayMillis = 90
                                    )
                                ))
                            .togetherWith(
                                fadeOut(tween(durationMillis = 220, delayMillis = 90)) +
                                        slideOutVertically(
                                            tween(
                                                durationMillis = 220,
                                                delayMillis = 90
                                            )
                                        )
                            )
                    },
                    contentAlignment = Alignment.Center
                ) {
                    when (it) {
                        true -> Icon(
                            imageVector = EyeOpenIcon,
                            contentDescription = null
                        )

                        false -> Icon(
                            imageVector = EyeCloseIcon,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        supportingText = supportingText?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        isError = isError,
        maxLines = 1,
        label = {
            Text(
                text = label,
                fontWeight = FontWeight.Light,
            )
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(.7f),
            unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(.7f),
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary.copy(.7f),
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary.copy(.7f),

            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,

            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    val isPasswordVisible = remember { mutableStateOf(false) }

    AppThem {
        Surface {
            AppPasswordField(
                text = "pass",
                onValueChange = { },
                onPasswordToggleClick = {
                    isPasswordVisible.value = !isPasswordVisible.value
                },
                label = "Password",
                leadingIcon = PasswordIcon,
                isPasswordVisible = isPasswordVisible.value
            )
        }
    }
}
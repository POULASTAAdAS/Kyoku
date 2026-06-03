package com.poulastaa.auth.ui.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.components.ElevatedGradientButton
import com.poulastaa.common.ui.components.top_bar.DefaultTopBar
import com.poulastaa.common.ui.design_system.dimens

private const val OTP_LENGTH = 5

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpScreen() {
    val email by remember { mutableStateOf("poulastaadas2@gmail.com") }
    var otp by remember { mutableStateOf("") }
    val isOTPError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val otpFontStyle = MaterialTheme.typography.headlineLarge
    var isValidating by remember { mutableStateOf(false) }
    val isValidOTP by remember(otp) { mutableStateOf(otp.length == OTP_LENGTH) }

    LaunchedEffect(otp.length == OTP_LENGTH) {
        isValidating = true
    }

    Scaffold(
        topBar = {
            DefaultTopBar(
                navigateBackEnabled = false,
                titleContent = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Verification",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                },
                onBackCLick = { /* No/Op */ },
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .padding(horizontal = MaterialTheme.dimens.layout.contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        MaterialTheme.typography.bodyMedium
                            .copy(color = MaterialTheme.colorScheme.primary)
                            .toSpanStyle(),
                    ) {
                        append("Please enter the verification code we have sent to email\n")
                    }
                    withStyle(
                        MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline,
                        ).toSpanStyle()
                    ) {
                        append(email)
                    }
                },
                textAlign = TextAlign.Center
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.small)

            BoxWithConstraints {
                BasicTextField(
                    modifier = Modifier.then(if (this.maxWidth > 780.dp) Modifier.fillMaxWidth(0.7f) else Modifier.fillMaxWidth()),
                    onValueChange = { str ->
                        str.takeIf { it.length <= OTP_LENGTH }?.let {
                            otp = it
                        }
                    },
                    value = otp,
                    decorationBox = { decorationBox ->
                        Row(
                            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(OTP_LENGTH) { index ->
                                val isCurrentSelected by remember(otp.length) { mutableStateOf(otp.length == index) }

                                Box(
                                    Modifier.wrapContentHeight()
                                        .padding(MaterialTheme.dimens.spacing.small)
                                        .then(
                                            if (isOTPError) Modifier.border(
                                                width = MaterialTheme.dimens.border.medium,
                                                color = MaterialTheme.colorScheme.error,
                                                shape = MaterialTheme.shapes.small
                                            )
                                            else Modifier.border(
                                                width = if (isCurrentSelected) MaterialTheme.dimens.border.medium else MaterialTheme.dimens.border.thin,
                                                color = if (isCurrentSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                                                shape = MaterialTheme.shapes.small
                                            )
                                        )
                                        .sizeIn(
                                            minWidth = otpFontStyle.fontSize.value.times(1.5).dp,
                                            minHeight = otpFontStyle.fontSize.value.dp,
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = otp.getOrElse(index) { ' ' }.toString(),
                                        style = otpFontStyle.copy(color = MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.padding(MaterialTheme.dimens.spacing.medium)
                                    )
                                }
                            }
                        }
                        Box(Modifier.graphicsLayer { alpha = 0f }) { decorationBox() }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.Transparent),
                )
            }

            ColumnSpacer(MaterialTheme.dimens.spacing.large)

            ElevatedGradientButton(
                width = 0.7f,
                enabled = isValidOTP,
                onClick = {
                    isValidating = true
                },
                colors = CardDefaults.elevatedCardColors(
                    contentColor = if (isValidOTP) MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.onBackground,
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isValidOTP) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small,
                        ).minimumInteractiveComponentSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Submit",
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.alpha(if (isValidating) 0f else 1f)
                    )
                    CircularProgressIndicator(
                        Modifier.alpha(if (isValidating) 1f else 0f),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        }
    }

    BackHandler {}
}
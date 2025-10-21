package com.poulastaa.auth.presentation.otp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.auth.presentation.components.ArchedScreen
import com.poulastaa.auth.presentation.components.ConformButton
import com.poulastaa.auth.presentation.otp.components.Info
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.ArrowBackIcon
import com.poulastaa.core.presentation.ui.MinusIcon
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OtpVerticalCompactScreen(
    modifier: Modifier = Modifier,
    width: Float = 1f,
    shake: Animatable<Float, AnimationVector1D>,
    state: OtpUiState,
    onAction: (OtpUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focus = LocalFocusManager.current

    LaunchedEffect(state.otp.value.length) {
        if (state.otp.value.length == 5) focus.clearFocus()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            VerificationTopBar(haptic, onAction)
        }
    ) { paddingValues ->
        ArchedScreen(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.medium1)
                    .then(modifier),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.weight(.25f))

                Info(
                    heading = state.email,
                    content = stringResource(R.string.verify_content)
                )

                Spacer(Modifier.height(MaterialTheme.dimens.medium2))

                OTPValidationTextField(width, state.otp, onAction, shake)

                ReSendOrNavigateBack(state, haptic, onAction)

                AnimatedVisibility(state.isTryAnotherEmailVisible) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Info(
                            heading = stringResource(R.string.try_other_mail),
                            content = stringResource(R.string.didnt_receive_otp),
                            isClickable = true,
                        ) {
                            onAction(OtpUiAction.OnDirectBack)
                        }
                    }
                }

                Spacer(Modifier.height(MaterialTheme.dimens.large2))

                ConformButton(
                    modifier = Modifier.fillMaxWidth(.4f),
                    isLoading = state.isLoading,
                    heading = stringResource(R.string.verify_text),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAction(OtpUiAction.OnSubmit)
                    }
                )

                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
internal fun ReSendOrNavigateBack(
    state: OtpUiState,
    haptic: HapticFeedback,
    onAction: (OtpUiAction.OnResendOTP) -> Unit,
    height: Dp = 80.dp,
) {
    AnimatedContent(
        targetState = state.otp.isErr,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = height)
    ) { isErr ->
        when (isErr) {
            true -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                if (state.otp.isErr) Text(
                    text = state.otp.errText.asString(),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.SemiBold
                )
                else Text(
                    text = stringResource(R.string.invalid_otp),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.SemiBold
                )
            }

            false -> AnimatedContent(state.resendState) { resendState ->
                when (resendState) {
                    ResendState.IDEAL -> Box(Modifier.fillMaxWidth())
                    ResendState.TICKER -> Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.resend_in),
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = state.ticker,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    }

                    ResendState.ENABLED -> Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.resend_otp),
                            fontWeight = FontWeight.Black,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                .clickable(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onAction(OtpUiAction.OnResendOTP)
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun OTPValidationTextField(
    width: Float,
    otp: TextProp,
    onAction: (OtpUiAction) -> Unit,
    shake: Animatable<Float, AnimationVector1D>,
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth(width),
        value = otp.value,
        onValueChange = {
            onAction(OtpUiAction.OnOTPChange(it))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(
            onSend = {
                onAction(OtpUiAction.OnSubmit)
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .height(80.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    val char = when {
                        index >= otp.value.length -> ""
                        else -> otp.value[index].toString()
                    }

                    val isFocused = otp.value.length == index

                    AnimatedContent(
                        targetState = char,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .aspectRatio(1f)
                            .offset(x = shake.value.dp),
                        transitionSpec = {
                            (fadeIn(animationSpec = tween(200)) +
                                    scaleIn(
                                        initialScale = 0.3f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )).togetherWith(
                                fadeOut(animationSpec = tween(100)) +
                                        scaleOut(
                                            targetScale = 1.2f,
                                            animationSpec = tween(150)
                                        )
                            )
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        val color = if (isFocused) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary

                        when {
                            it == "" -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .border(
                                            width = if (isFocused) 1.5.dp else 1.dp,
                                            color = if (otp.isErr) MaterialTheme.colorScheme.errorContainer
                                            else color,
                                            shape = MaterialTheme.shapes.small
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            modifier = Modifier.fillMaxSize(.5f),
                                            imageVector = MinusIcon,
                                            contentDescription = null,
                                            tint = if (otp.isErr) MaterialTheme.colorScheme.error
                                            else color,
                                        )
                                    }
                                }
                            }

                            else -> Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = if (isFocused) 1.5.dp else 1.dp,
                                        color = if (otp.isErr) MaterialTheme.colorScheme.errorContainer
                                        else color,
                                        shape = MaterialTheme.shapes.small
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(MaterialTheme.dimens.small1),
                                    text = char,
                                    textAlign = TextAlign.Center,
                                    fontSize = 50.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (otp.isErr) MaterialTheme.colorScheme.error
                                    else color,
                                )
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier.size(0.dp)) { innerTextField() }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VerificationTopBar(
    haptic: HapticFeedback,
    onAction: (OtpUiAction.OnStateNavigateBackFlow) -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.verification),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(OtpUiAction.OnStateNavigateBackFlow)
                }
            ) {
                Icon(
                    imageVector = ArrowBackIcon,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}


@PreviewCompactPortrait
@Composable
private fun Preview() {
    var state by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val shake = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                state = !state
                delay(5_000)
            }
        }
    }

    LaunchedEffect(state) {
        if (state) {
            shake.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 500

                    0f at 0
                    -12f at 40
                    12f at 80
                    -12f at 120
                    12f at 160
                    -10f at 200
                    10f at 240
                    -8f at 280
                    8f at 320
                    -5f at 360
                    5f at 400
                    -3f at 440
                    3f at 480
                    0f at 500
                }
            )
        }
    }

    AppTheme(isSystemInDarkTheme()) {
        OtpVerticalCompactScreen(
            shake = shake,
            state = OtpUiState(
                resendState = ResendState.IDEAL,
                email = "poulastaadas2@gmail.com",
                otp = TextProp(
                    value = "1234",
                    isErr = state,
                    errText = UiText.StringResource(R.string.invalid_otp)
                ),
            )
        ) { }
    }
}

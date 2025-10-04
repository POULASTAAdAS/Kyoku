package com.poulastaa.core.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource

val GoogleIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.ic_google)

val AppLogo: ImageVector
    @Composable
    get() = if (isSystemInDarkTheme()) AppDarkLogo else AppLightLogo

val AppDarkLogo: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_logo_dark)

val AppLightLogo: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_logo_light)

val EmailIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_email)

val AlternateEmailIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_alternate_email)

val CheckIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_check)

val PasswordIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_password)

val EyeOpenIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_visibility_on)

val EyeCloseIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_visibility_off)

val UserIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_user)

val ArrowBackIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_arrow_back)

val MinusIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_minus)

val CloseIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_close)


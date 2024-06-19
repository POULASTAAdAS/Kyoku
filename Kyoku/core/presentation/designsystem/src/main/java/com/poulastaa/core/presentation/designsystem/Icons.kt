package com.poulastaa.core.presentation.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource

val AppLogo: Painter
    @Composable
    get() = if (isSystemInDarkTheme()) painterResource(id = R.drawable.ic_app_logo_night)
    else painterResource(id = R.drawable.ic_app_logo_light)

val GoogleIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.ic_google)

val EmailIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_email)

val CheckIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_check)

val PasswordIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_password)

val EyeOpenIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_visibility_on)

val EyeCloseIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_visibility_off)

val UserIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_user)

val ArrowBackIcon: ImageVector
    @Composable
    get() = Icons.AutoMirrored.Rounded.ArrowBack

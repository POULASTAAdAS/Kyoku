package com.poulastaa.common.ui.design_system

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import kyoku.common.ui.generated.resources.Res
import kyoku.common.ui.generated.resources.ic_add_link
import kyoku.common.ui.generated.resources.ic_app_logo_dark
import kyoku.common.ui.generated.resources.ic_app_logo_light
import kyoku.common.ui.generated.resources.ic_arrow_back
import kyoku.common.ui.generated.resources.ic_artist
import kyoku.common.ui.generated.resources.ic_check
import kyoku.common.ui.generated.resources.ic_close
import kyoku.common.ui.generated.resources.ic_email
import kyoku.common.ui.generated.resources.ic_eye_close
import kyoku.common.ui.generated.resources.ic_eye_open
import kyoku.common.ui.generated.resources.ic_genre
import kyoku.common.ui.generated.resources.ic_google
import kyoku.common.ui.generated.resources.ic_password_type_key
import kyoku.common.ui.generated.resources.ic_password_type_lock
import kyoku.common.ui.generated.resources.ic_show_more
import kyoku.common.ui.generated.resources.ic_user
import org.jetbrains.compose.resources.vectorResource


val IconAppDark: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_app_logo_dark)

val IconAppLogo: ImageVector
    @Composable
    get() = if (isSystemInDarkTheme()) IconAppDark else IconAppLight

val IconAppLight: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_app_logo_light)

val IconEmail: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_email)

val IconPasswordKey: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_password_type_key)

val IconPasswordLock: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_password_type_lock)


val IconEyeOpen: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_eye_open)


val IconEyeClose: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_eye_close)

val IconShowMore: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_show_more)


val IconGoogle: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_google)

val IconArrowBack: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_arrow_back)

val IconUser: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_user)

val IconAddLink: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_add_link)

val IconClose: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_close)

val IconGenre: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_genre)

val IconCheck: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_check)

val IconArtist: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_artist)
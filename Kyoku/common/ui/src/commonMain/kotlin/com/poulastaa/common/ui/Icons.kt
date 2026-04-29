package com.poulastaa.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import kyoku.common.ui.generated.resources.Res
import kyoku.common.ui.generated.resources.ic_email
import kyoku.common.ui.generated.resources.ic_eye_close
import kyoku.common.ui.generated.resources.ic_eye_open
import kyoku.common.ui.generated.resources.ic_password_type_key
import kyoku.common.ui.generated.resources.ic_password_type_lock
import kyoku.common.ui.generated.resources.ic_show_more
import org.jetbrains.compose.resources.vectorResource


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
package com.poulastaa.kyoku.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val small1: Dp = 4.dp,
    val small2: Dp = 6.dp,
    val small3: Dp = 8.dp,
    val medium1: Dp = 16.dp,
    val medium2: Dp = 18.dp,
    val medium3: Dp = 24.dp,
    val buttonHeight: Dp = 0.dp,
    val roundedCornerShape1: Dp = 4.dp,
    val roundedCornerShape2: Dp = 16.dp,
    val roundedCornerShape3: Dp = 32.dp
)

val CompactSmallDimens = Dimens(
    small1 = 2.dp,
    small2 = 4.dp,
    small3 = 6.dp,
    medium1 = 10.dp,
    medium2 = 14.dp,
    medium3 = 20.dp,
    buttonHeight = 38.dp
)

val CompactMediumDimens = Dimens( // default
    small1 = 4.dp,
    small2 = 6.dp,
    small3 = 8.dp,
    medium1 = 16.dp,
    medium2 = 18.dp,
    medium3 = 24.dp,
    buttonHeight = 52.dp
)


val CompactDimens = Dimens( // todo not tested
    small1 = 4.dp,
    small2 = 6.dp,
    small3 = 8.dp,
    medium1 = 16.dp,
    medium2 = 18.dp,
    medium3 = 24.dp,
    buttonHeight = 90.dp
)


val MediumDimens = Dimens( // todo not tested
    small1 = 4.dp,
    small2 = 8.dp,
    small3 = 16.dp,
    medium1 = 20.dp,
    medium2 = 24.dp,
    medium3 = 28.dp,
    buttonHeight = 90.dp
)

val ExpandedDimens = Dimens( // todo not tested
    small1 = 4.dp,
    small2 = 8.dp,
    small3 = 16.dp,
    medium1 = 20.dp,
    medium2 = 24.dp,
    medium3 = 28.dp,
    buttonHeight = 90.dp
)

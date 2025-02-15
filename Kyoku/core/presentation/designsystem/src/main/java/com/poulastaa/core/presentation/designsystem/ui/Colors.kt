package com.poulastaa.core.presentation.designsystem.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

internal val primaryLight = Color(0xFF2F5136)
internal val secondaryLight = Color(0xFF588157)
internal val tertiaryLight = Color(0xFFA3B18A)
internal val backgroundLight = Color(0xFFABC7B1)
internal val containerLight = Color(0xFF8BAC92)
internal val errorLight = Color(0xFF932A2A)
internal val errorContainerLight = Color(0xFFF9DEDC)

internal val primaryDark = Color(0xFF88AD99)
internal val secondaryDark = Color(0xFF3C6144)
internal val tertiaryDark = Color(0xFF627B64)
internal val backgroundDark = Color(0xFF1D3327)
internal val containerDark = Color(0xFF405C4D)
internal val errorDark = Color(0xFFDBACA9)
internal val errorContainerDark = Color(0xFF622C2C)

@Stable
@Composable
fun gradiantBackground() = when (isSystemInDarkTheme()) {
    true -> listOf(
        backgroundDark,
        secondaryDark
    )

    false -> listOf(
        backgroundLight,
        secondaryLight
    )
}

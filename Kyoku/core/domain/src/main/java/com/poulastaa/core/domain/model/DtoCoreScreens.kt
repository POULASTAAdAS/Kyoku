package com.poulastaa.core.domain.model

sealed interface DtoCoreScreens {
    data object Profile : DtoCoreScreens
    data object History : DtoCoreScreens
    data object Settings : DtoCoreScreens
    data object ToggleTheme : DtoCoreScreens
}
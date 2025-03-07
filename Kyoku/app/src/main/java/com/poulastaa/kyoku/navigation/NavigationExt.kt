package com.poulastaa.kyoku.navigation

import com.poulastaa.core.domain.model.DtoCoreScreens

fun DtoCoreScreens.toCoreScreen() = when (this) {
    DtoCoreScreens.History -> Screens.Core.History
    DtoCoreScreens.Profile -> Screens.Core.Profile
    DtoCoreScreens.Settings -> Screens.Core.Settings
    else -> throw IllegalArgumentException("Navigation for Toggle Them should not be triggered when converting to Screens.Core.Settings On File NavigationExt")
}
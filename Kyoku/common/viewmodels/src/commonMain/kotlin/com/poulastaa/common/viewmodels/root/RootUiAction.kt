package com.poulastaa.common.viewmodels.root

import com.poulastaa.common.viewmodels.Screens

sealed interface RootUiAction {
    data class OnDestinationChanged(val screen: Screens) : RootUiAction
}

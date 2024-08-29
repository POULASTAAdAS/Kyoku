package com.poulastaa.play.presentation.settings

import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.ui.UiText

sealed interface SettingUiAction {
    data class Navigate(val screen: ScreenEnum) : SettingUiAction
    data class EmitToast(val message: UiText) : SettingUiAction
}
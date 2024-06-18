package com.poulastaa.auth.presentation.intro

import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.ui.UiText

sealed interface IntroUiAction {
    data class OnSuccess(val screen: ScreenEnum) : IntroUiAction
    data class EmitToast(val message: UiText): IntroUiAction
}
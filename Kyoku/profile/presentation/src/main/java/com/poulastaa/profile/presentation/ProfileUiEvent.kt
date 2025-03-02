package com.poulastaa.profile.presentation

import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.profile.domain.model.ProfileAllowedNavigationScreen

internal sealed interface ProfileUiEvent {
    data class EmitToast(val message: UiText) : ProfileUiEvent
    data class Navigate(val screen: ProfileAllowedNavigationScreen) : ProfileUiEvent
}
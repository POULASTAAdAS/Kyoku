package com.poulastaa.profile.presentation

internal sealed interface ProfileUiAction {
    data object OnEditProfileImage : ProfileUiAction
    data object OnEditUserName : ProfileUiAction
    data class OnUserNameChange(val username: String) : ProfileUiAction
    data class OnNavigateSavedItem(val type: UiItemType) : ProfileUiAction

    data object OnNavigateToGallery : ProfileUiAction
}
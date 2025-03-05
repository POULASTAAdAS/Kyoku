package com.poulastaa.profile.presentation

internal sealed interface ProfileUiAction {
    data object OnEditProfileImage : ProfileUiAction
    data object OnEditUserNameToggle : ProfileUiAction
    data class OnUserNameChange(val username: String) : ProfileUiAction
    data object OnConformUserName : ProfileUiAction
    data class OnNavigateSavedItem(val type: UiItemType) : ProfileUiAction

    data object OnNavigateToLibrary : ProfileUiAction
}
package com.poulastaa.main.presentation.home

internal sealed interface HomeUiAction {
    data class OnSavedItemCLick(val id: Long, val type: UiSaveItemType) : HomeUiAction
}
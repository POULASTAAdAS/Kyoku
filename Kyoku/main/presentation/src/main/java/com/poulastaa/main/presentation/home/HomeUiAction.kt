package com.poulastaa.main.presentation.home

import com.poulastaa.main.presentation.components.UiSaveItemType

internal sealed interface HomeUiAction {
    data class OnSavedItemCLick(val id: Long, val type: UiSaveItemType) : HomeUiAction
}
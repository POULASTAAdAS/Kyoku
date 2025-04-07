package com.poulastaa.view.presentation.saved

import com.poulastaa.core.presentation.designsystem.model.ItemClickType

internal sealed interface ViewSavedUiAction {
    data object OnEditToggle : ViewSavedUiAction
    data class OnItemClick(val id: Long, val clickType: ItemClickType) : ViewSavedUiAction
    data object OnAddNewItemClick : ViewSavedUiAction
}
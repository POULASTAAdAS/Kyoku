package com.poulastaa.kyoku.data.model.screens.home

import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.library.PinnedDataType

sealed class HomeUiEvent {
    data class EmitToast(val message: String) : HomeUiEvent()
    data object SomethingWentWrong : HomeUiEvent()
    data class ItemClick(
        val type: ItemsType,
        val id: Long = 0,
        val name: String = "name",
        val isApiCall: Boolean = false
    ) : HomeUiEvent()

    data class ItemLongClick(
        val type: HomeLongClickType,
        val id: Long = 0,
        val name: String = "name"
    ) : HomeUiEvent()

    sealed class BottomSheetItemClick : HomeUiEvent() {
        data class AddClick(val type: PinnedDataType, val name: String) : BottomSheetItemClick()
        data object RemoveClick : BottomSheetItemClick()
        data object DeleteClick : BottomSheetItemClick()
    }
}

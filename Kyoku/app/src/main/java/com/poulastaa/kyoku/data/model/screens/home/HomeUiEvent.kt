package com.poulastaa.kyoku.data.model.screens.home

import com.poulastaa.kyoku.data.model.screens.common.ItemsType

sealed class HomeUiEvent {
    data class EmitToast(val message: String) : HomeUiEvent()
    data object SomethingWentWrong : HomeUiEvent()
    data class ItemClick(
        val type: ItemsType,
        val id: Long? = null,
        val name: String? = null
    ) : HomeUiEvent()
}

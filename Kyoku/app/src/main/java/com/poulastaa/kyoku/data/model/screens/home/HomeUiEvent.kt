package com.poulastaa.kyoku.data.model.screens.home

sealed class HomeUiEvent {
    data class EmitToast(val message: String) : HomeUiEvent()
    data object SomethingWentWrong : HomeUiEvent()
}

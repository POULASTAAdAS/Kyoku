package com.poulastaa.board.presentation.set_bdate

internal sealed interface SetUpBDateUiAction {
    data class OnBDateChange(val date: String) : SetUpBDateUiAction
    data object OnSubmitClick : SetUpBDateUiAction
}
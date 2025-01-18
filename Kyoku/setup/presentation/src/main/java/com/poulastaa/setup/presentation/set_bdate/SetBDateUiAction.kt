package com.poulastaa.setup.presentation.set_bdate

sealed interface SetBDateUiAction {
    data object OnBackClick : SetBDateUiAction
    data object OnDateDialogToggle : SetBDateUiAction
    data class OnDateChange(val date: Long) : SetBDateUiAction
    data object OnSubmitClick : SetBDateUiAction
}
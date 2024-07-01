package com.poulastaa.setup.presentation.set_b_date

sealed interface BDateUiEvent {
    data object OnBackClick : BDateUiEvent
    data object OnBDateDialogToggle : BDateUiEvent
    data class OnBDateSubmit(val value: Long?) : BDateUiEvent
    data object OnContinueClick : BDateUiEvent
}
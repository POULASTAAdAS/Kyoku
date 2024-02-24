package com.poulastaa.kyoku.data.model.screens.setup.set_b_date

sealed class SetBirthDateUiEvent {
    data object OnDateSelectorClicked : SetBirthDateUiEvent()

    data class OnDateSelected(val date: String) : SetBirthDateUiEvent()

    data object OnContinueClick : SetBirthDateUiEvent()

    data class EmitToast(val message: String) : SetBirthDateUiEvent()
}
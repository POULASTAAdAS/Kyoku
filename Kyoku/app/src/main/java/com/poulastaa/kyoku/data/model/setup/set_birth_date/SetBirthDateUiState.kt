package com.poulastaa.kyoku.data.model.setup.set_birth_date

data class SetBirthDateUiState(
    val bDate: String = "",
    val isDialogOpen: Boolean = false,
    val isInternetAvailable: Boolean = false,
    val isError: Boolean = true,
    val isLoading:Boolean = false
)

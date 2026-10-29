package com.poulastaa.common.viewmodels.states

data class UiTextFiledState(
    val value: String = "",
    val isError: Boolean = false,
    val errorMessage: String? = null,
)

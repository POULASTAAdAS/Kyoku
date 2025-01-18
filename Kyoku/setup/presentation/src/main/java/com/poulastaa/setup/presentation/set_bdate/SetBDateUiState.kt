package com.poulastaa.setup.presentation.set_bdate

import com.poulastaa.core.presentation.ui.model.TextHolder

data class SetBDateUiState(
    val isMakingApiCall: Boolean = false,
    val date: TextHolder = TextHolder(),

    val isDialogOpn: Boolean = false,
)

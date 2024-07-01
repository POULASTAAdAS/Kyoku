package com.poulastaa.setup.presentation.set_b_date

import com.poulastaa.core.presentation.ui.model.TextHolder

data class BDateUiState(
    val isMakingApiCall: Boolean = false,

    val isDateDialogOpen: Boolean = false,
    val canMakeReq: Boolean = false,
    val bDate: TextHolder = TextHolder(),
)

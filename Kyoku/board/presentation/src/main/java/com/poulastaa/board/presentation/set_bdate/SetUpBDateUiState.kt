package com.poulastaa.board.presentation.set_bdate

import androidx.compose.runtime.Stable
import com.poulastaa.core.presentation.designsystem.TextProp

@Stable
internal data class SetUpBDateUiState(
    val isMakingApiCall: Boolean = false,
    val bDate: TextProp = TextProp(),
)

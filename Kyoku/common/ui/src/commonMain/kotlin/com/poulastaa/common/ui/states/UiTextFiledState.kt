package com.poulastaa.common.ui.states

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
data class UiTextFiledState(
    val value: String = "",
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isSupportingText: Boolean = false,
    val supportingText: String? = null,
)

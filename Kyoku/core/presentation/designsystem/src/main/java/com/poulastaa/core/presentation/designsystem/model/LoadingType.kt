package com.poulastaa.core.presentation.designsystem.model

import androidx.annotation.RawRes

sealed class LoadingType {
    enum class ERROR_TYPE {
        NO_INTERNET,
        UNKNOWN
    }

    data object Loading : LoadingType()
    data class Error(
        val type: ERROR_TYPE,
        @RawRes val lottieId: Int,
    ) : LoadingType()

    data object Content : LoadingType()
}
package com.poulastaa.kyoku.data.model

data class BDateFroMaterHelper(
    val date: String,
    val status: BDateFroMaterHelperStatus
)

enum class BDateFroMaterHelperStatus {
    OK,
    TO_OLD,
    TO_YOUNG,
    FROM_FUTURE
}
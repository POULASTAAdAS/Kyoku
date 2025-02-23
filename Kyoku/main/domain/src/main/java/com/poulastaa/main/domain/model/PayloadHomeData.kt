package com.poulastaa.main.domain.model

data class PayloadHomeData(
    val savedItems: List<PayloadSavedItem>,
    val staticData: PayloadStaticData,
)

package com.poulastaa.core.domain.model

data class PinnedData(
    val id: Long,
    val name: String,
    val urls: List<String>,
    val pinnedType: PinnedType,
)

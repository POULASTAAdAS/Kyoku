package com.poulastaa.main.domain.model

import com.poulastaa.core.domain.model.PlaylistId

data class PayloadSavedItem(
    val id: PlaylistId,
    val name: String,
    val posters: List<String>,
    val type: PayloadSaveItemType,
)

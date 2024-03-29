package com.poulastaa.kyoku.data.model.screens.common

import androidx.compose.runtime.Stable

@Stable
data class UiAlbum(
    val id: Long,
    val name: String,
    val coverImage: String
)

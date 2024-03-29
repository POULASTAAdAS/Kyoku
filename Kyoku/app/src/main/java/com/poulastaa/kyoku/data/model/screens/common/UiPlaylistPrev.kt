package com.poulastaa.kyoku.data.model.screens.common

import androidx.compose.runtime.Stable

@Stable
data class UiPlaylistPrev(
    val id: Long,
    val name: String,
    val listOfUrl: List<String>
)
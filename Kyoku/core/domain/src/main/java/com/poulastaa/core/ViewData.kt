package com.poulastaa.core

import com.poulastaa.core.domain.ViewSong

data class ViewData(
    val id: Long = -1,
    val name: String = "",
    val listOfSong: List<ViewSong> = emptyList()
)

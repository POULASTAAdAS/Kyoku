package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class Favourites(
    val listOfSongs:List<ResponseSong> = emptyList()
)

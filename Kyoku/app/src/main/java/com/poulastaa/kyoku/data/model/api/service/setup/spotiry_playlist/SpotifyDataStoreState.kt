package com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist

import com.poulastaa.kyoku.data.model.api.auth.AuthType

data class SpotifyDataStoreState(
    val tokenOrCookie: String = "",
    val authType: AuthType = AuthType.UN_AUTH,
    val isCookie: Boolean = false
)

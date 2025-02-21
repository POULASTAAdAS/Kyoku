package com.poulastaa.setup.network

import com.poulastaa.core.network.mapper.toDtoGenre
import com.poulastaa.core.network.mapper.toDtoPrevArtist
import com.poulastaa.setup.domain.model.SetBDateStatus
import com.poulastaa.setup.network.model.SetBDateStatusRes
import com.poulastaa.setup.network.model.SuggestedArtistRes
import com.poulastaa.setup.network.model.SuggestedGenreRes

internal fun SetBDateStatusRes.toSetBDateStatus() = when (this) {
    SetBDateStatusRes.SUCCESS -> SetBDateStatus.SUCCESS
    SetBDateStatusRes.FAILURE -> SetBDateStatus.FAILURE
}

internal fun SuggestedGenreRes.toDtoGenre() = this.list.map {
    it.toDtoGenre()
}

internal fun SuggestedArtistRes.toDtoPrevArtist() = this.list.map {
    it.toDtoPrevArtist()
}
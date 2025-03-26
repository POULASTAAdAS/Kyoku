package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.model.DtoSuggestedArtistSongRelation
import com.poulastaa.core.domain.model.SongId

interface LocalRefreshDatasource {
    suspend fun clearSuggestedData()

    suspend fun updatePopularSongMix(list: List<DtoPrevSong>)
    suspend fun updatePopularArtistMix(list: List<DtoPrevSong>)
    suspend fun updateOldGem(list: List<DtoPrevSong>)

    suspend fun updateSuggestedArtist(list: List<DtoPrevArtist>)
    suspend fun updateSuggestedAlbum(list: List<DtoPrevAlbum>)

    suspend fun storePrevSong(list: List<DtoPrevSong>)
    suspend fun storeSuggestedArtist(list: List<DtoPrevArtist>)
    suspend fun updateRelationSuggestedArtistSong(list: List<DtoRelationSuggestedArtistSong>)

    suspend fun getSavedMostPopularSongs(): List<SongId>
    suspend fun getSavedPopularArtistMix(): List<SongId>
    suspend fun getSavedOldGem(): List<SongId>

    suspend fun getSavedSuggestedArtist(): List<ArtistId>
    suspend fun getSavedSuggestedAlbum(): List<ArtistId>
    suspend fun getOldSuggestedArtistSongRelation(): List<DtoSuggestedArtistSongRelation>
}
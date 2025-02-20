package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.CountryId
import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoCountry
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoSuggestedType
import com.poulastaa.core.domain.model.GenreId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId

interface LocalHomeDatasource {
    suspend fun storeSong(song: DtoSong): SongId
    suspend fun storeSong(list: List<DtoSong>): List<SongId>

    suspend fun storePrevSong(list: List<DtoPrevSong>): List<SongId>

    suspend fun storeArtist(artist: DtoArtist): ArtistId
    suspend fun storeArtist(list: List<DtoArtist>): List<ArtistId>

    suspend fun storePrevArtist(artist: DtoPrevArtist): ArtistId
    suspend fun storePrevArtist(list: List<DtoPrevArtist>): List<ArtistId>

    suspend fun storePlaylist(playlist: DtoPlaylist): PlaylistId
    suspend fun storePlaylist(list: List<DtoPlaylist>): List<PlaylistId>

    suspend fun storeAlbum(album: DtoAlbum): AlbumId
    suspend fun storeAlbum(list: List<DtoAlbum>): List<AlbumId>

    suspend fun storeGenre(genre: DtoGenre): GenreId
    suspend fun storeGenre(list: List<DtoGenre>): List<GenreId>

    suspend fun storeCountry(country: DtoCountry): CountryId
    suspend fun storeCountry(list: List<DtoCountry>): List<CountryId>

    suspend fun storePrevExploreType(type: DtoExploreType, data: List<DtoPrevSong>)

    suspend fun storeRelationSuggested(type: DtoSuggestedType, data: List<Long>)
    suspend fun storeRelationSuggestedArtistSong(list: List<DtoRelationSuggestedArtistSong>)
}
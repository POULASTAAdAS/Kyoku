package com.poulastaa.setup.network.model

import com.poulastaa.common.domain.model.DtoAlbum
import com.poulastaa.common.domain.model.DtoArtist
import com.poulastaa.common.domain.model.DtoComposer
import com.poulastaa.common.domain.model.DtoCountry
import com.poulastaa.common.domain.model.DtoGenre
import com.poulastaa.common.domain.model.DtoSong
import com.poulastaa.common.domain.model.DtoSongInfo
import kotlinx.serialization.Serializable

@Serializable
data class SongResponse(
    val songId: Long,
    val title: String,
    val poster: String? = null,
    val info: SongInfoResponse? = null,
    val artists: List<ArtistResponse> = emptyList(),
    val genre: List<GenreResponse> = emptyList(),
    val country: List<CountryResponse> = emptyList(),
    val albums: List<AlbumResponse> = emptyList(),
) {
    fun toDto() = DtoSong(
        id = songId,
        title = title,
        poster = poster,
        info = info?.toDto(),
        artists = artists.map(ArtistResponse::toDto),
        genres = genre.map(GenreResponse::toDto),
        countries = country.map(CountryResponse::toDto),
        albums = albums.map(AlbumResponse::toDto),
    )
}

@Serializable
data class SongInfoResponse(
    val songId: Long,
    val releaseYear: Int = 0,
    val composers: List<ComposerResponse> = emptyList(),
    val popularity: Long = 0,
) {
    fun toDto() = DtoSongInfo(
        songId = songId,
        releaseYear = releaseYear,
        composers = composers.map(ComposerResponse::toDto),
        popularity = popularity,
    )
}

@Serializable
data class ComposerResponse(
    val id: Long,
    val name: String,
    val coverImage: String? = null,
    val followers: Long = 0,
) {
    fun toDto() = DtoComposer(
        id = id,
        name = name,
        coverImage = coverImage,
        followers = followers,
    )
}

@Serializable
data class ArtistResponse(
    val id: Long,
    val name: String,
    val coverImage: String? = null,
    val followers: Long = 0,
    val birthDate: String? = null,
    val biography: String? = null,
    val monthlyListeners: Long = 0,
    val albums: List<AlbumResponse> = emptyList(),
    val genres: List<GenreResponse> = emptyList(),
) {
    fun toDto(): DtoArtist = DtoArtist(
        id = id,
        name = name,
        coverImage = coverImage,
        followers = followers,
        birthDate = birthDate,
        biography = biography,
        monthlyListeners = monthlyListeners,
        albums = albums.map(AlbumResponse::toDto),
        genres = genres.map(GenreResponse::toDto),
    )
}

@Serializable
data class AlbumResponse(
    val id: Long,
    val name: String,
    val popularity: Long = 0,
    val poster: String? = null,
    val artists: List<ArtistResponse> = emptyList(),
) {
    fun toDto(): DtoAlbum = DtoAlbum(
        id = id,
        name = name,
        popularity = popularity,
        poster = poster,
        artists = artists.map(ArtistResponse::toDto),
    )
}

@Serializable
data class GenreResponse(
    val id: Int,
    val name: String,
    val coverImage: String? = null,
    val popularity: Long = 0,
) {
    fun toDto() = DtoGenre(
        id = id,
        name = name,
        coverImage = coverImage,
        popularity = popularity,
    )
}

@Serializable
data class CountryResponse(
    val id: Int,
    val country: String,
    val code: String,
) {
    fun toDto() = DtoCountry(
        id = id,
        name = country,
        code = code,
    )
}

package com.poulastaa.common.domain.model

data class DtoSong(
    val id: Long,
    val title: String,
    val poster: String? = null,
    val info: DtoSongInfo? = null,
    val artists: List<DtoArtist> = emptyList(),
    val genres: List<DtoGenre> = emptyList(),
    val countries: List<DtoCountry> = emptyList(),
    val albums: List<DtoAlbum> = emptyList(),
)

data class DtoSongInfo(
    val songId: Long,
    val releaseYear: Int,
    val composers: List<DtoComposer> = emptyList(),
    val popularity: Long = 0,
)

data class DtoComposer(
    val id: Long,
    val name: String,
    val coverImage: String? = null,
    val followers: Long = 0,
)

data class DtoArtist(
    val id: Long,
    val name: String,
    val coverImage: String? = null,
    val followers: Long = 0,
    val birthDate: String? = null,
    val biography: String? = null,
    val monthlyListeners: Long = 0,
    val albums: List<DtoAlbum> = emptyList(),
    val genres: List<DtoGenre> = emptyList(),
)

data class DtoAlbum(
    val id: Long,
    val name: String,
    val popularity: Long = 0,
    val poster: String? = null,
    val artists: List<DtoArtist> = emptyList(),
)

data class DtoGenre(
    val id: Int,
    val name: String,
    val coverImage: String? = null,
    val popularity: Long = 0,
)

data class DtoCountry(
    val id: Int,
    val name: String,
    val code: String,
)

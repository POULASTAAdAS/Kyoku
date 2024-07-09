package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.DayTypeSongPrevEntity
import com.poulastaa.core.database.entity.FavouriteArtistMixPrevEntity
import com.poulastaa.core.database.entity.PopularAlbumPrevEntity
import com.poulastaa.core.database.entity.PopularSongFromYourTimePrevEntity
import com.poulastaa.core.database.entity.PopularSongMixPrevEntity
import com.poulastaa.core.database.entity.PopularSuggestArtistEntity
import com.poulastaa.core.database.entity.popular_artist_song.ArtistSongEntity
import com.poulastaa.core.database.entity.popular_artist_song.PopularSongArtistEntity
import com.poulastaa.core.database.model.PopularArtistWithSongResult
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.PrevAlbum
import com.poulastaa.core.domain.model.PrevArtistSong
import com.poulastaa.core.domain.model.PrevSong
import com.poulastaa.core.domain.model.PrevSongDetail

fun PrevSong.toPopularSongMixPrevEntity() = PopularSongMixPrevEntity(
    id = this.songId,
    coverImage = this.coverImage
)

fun PrevSong.toPopularSongFromYourTimePrevEntity() = PopularSongFromYourTimePrevEntity(
    id = this.songId,
    coverImage = this.coverImage
)

fun PrevSong.toFavouriteArtistMixPrevEntity() = FavouriteArtistMixPrevEntity(
    id = this.songId,
    coverImage = this.coverImage
)

fun PrevSong.toDayTypeSongPrevEntity(dayType: DayType) = DayTypeSongPrevEntity(
    id = this.songId,
    dayType = dayType,
    coverImage = this.coverImage
)

fun PrevAlbum.toPopularAlbumPrevEntity() = PopularAlbumPrevEntity(
    id = this.albumId,
    name = this.name,
    coverImage = this.coverImage
)

fun Artist.toPopularSuggestArtistEntity() = PopularSuggestArtistEntity(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage ?: ""
)

fun Artist.toPopularSongArtistEntity() = PopularSongArtistEntity(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage ?: ""
)

fun PrevSongDetail.toArtistSongEntity() = ArtistSongEntity(
    id = this.songId,
    title = this.title,
    coverImage = this.coverImage
)


fun PopularSongMixPrevEntity.toPopularSongMixPrev() = PrevSong(
    songId = this.id,
    coverImage = this.coverImage
)

fun PopularSongFromYourTimePrevEntity.toPopularSongFromYourTimePrev() = PrevSong(
    songId = this.id,
    coverImage = this.coverImage
)

fun FavouriteArtistMixPrevEntity.toFavouriteArtistMixPrev() = PrevSong(
    songId = this.id,
    coverImage = this.coverImage
)

fun DayTypeSongPrevEntity.toDayTypeSongPrev() = PrevSong(
    songId = this.id,
    coverImage = this.coverImage
)

fun PopularAlbumPrevEntity.toPopularAlbumPrev() = PrevAlbum(
    albumId = this.id,
    name = this.name,
    coverImage = this.coverImage
)

fun PopularSuggestArtistEntity.toSuggestArtist() = Artist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage
)


fun Map.Entry<Long, List<PopularArtistWithSongResult>>.toPrevArtistSong() = PrevArtistSong(
    artist = Artist(
        id = this.key,
        name = this.value.first().name,
        coverImage = this.value.first().artistCover
    ),
    songs = this.value.map {
        PrevSongDetail(
            songId = it.songId,
            title = it.title,
            coverImage = it.coverImage
        )
    }
)




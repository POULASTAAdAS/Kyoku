package com.poulastaa.data.mappers

import com.poulastaa.data.model.SongDto
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.repository.DatabaseRepository
import com.poulastaa.domain.table.SongTable
import com.poulastaa.utils.Constants.ARTIST_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.MASTER_PLAYLIST_ROOT_DIR
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.ResultRow

fun String.constructSongCoverImage() = "${System.getenv("SERVICE_URL") + EndPoints.GetCoverImage.route}?coverImage=${
    this.replace(COVER_IMAGE_ROOT_DIR, "")
}"

fun String.constructMasterPlaylistUrl() = "${System.getenv("SERVICE_URL") + EndPoints.GetCoverImage.route}?master=${
    this.replace(MASTER_PLAYLIST_ROOT_DIR, "")
}"

fun String.constructArtistProfileUrl() = "${System.getenv("SERVICE_URL") + EndPoints.GetArtistImage.route}?artistCover=${
    this.replace(ARTIST_IMAGE_ROOT_DIR, "").replace(" ", "_")
}"

suspend fun ResultRow.toSongDto(database: DatabaseRepository) = coroutineScope {
    val artist = database.getArtistOnSongId(this@toSongDto[SongTable.id].value)

    SongDto(
        id = this@toSongDto[SongTable.id].value,
        coverImage = this@toSongDto[SongTable.coverImage].constructSongCoverImage(),
        title = this@toSongDto[SongTable.title],
        artistName = artist.joinToString { resultArtist -> resultArtist.name },
        releaseYear = this@toSongDto[SongTable.year],
        masterPlaylistUrl = this@toSongDto[SongTable.masterPlaylistPath].constructMasterPlaylistUrl()
    )
}
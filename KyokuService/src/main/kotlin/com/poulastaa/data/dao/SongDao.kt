package com.poulastaa.data.dao

import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.table.SongTable
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.MASTER_PLAYLIST_ROOT_DIR
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SongDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SongDao>(SongTable)

    private val coverImage by SongTable.coverImage
    private val masterPlaylistPath by SongTable.masterPlaylistPath
    val totalTime by SongTable.totalTime
    val title by SongTable.title
    val composer by SongTable.composer
    val publisher by SongTable.publisher
    val albumArtist by SongTable.album_artist
    val track by SongTable.track
    val year by SongTable.year
    var points by SongTable.points

    fun constructCoverImage() = "${System.getenv("SERVICE_URL") + EndPoints.GetCoverImage.route}?coverImage=${
        this.coverImage.replace(COVER_IMAGE_ROOT_DIR, "")
    }"


    fun constructMasterPlaylistUrl() = "${System.getenv("SERVICE_URL") + EndPoints.PlaySongMaster.route}?master=${
        this.masterPlaylistPath.replace(MASTER_PLAYLIST_ROOT_DIR, "")
    }"
}
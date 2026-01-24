package com.poulastaa.kyoku.playlist.database.playlist.repository

import com.poulastaa.kyoku.playlist.database.playlist.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.EntitySongPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.ids.SongPlaylistId
import com.poulastaa.kyoku.playlist.utils.PlaylistId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface PlaylistDataSource : JpaRepository<EntityPlaylist, PlaylistId> {

    @Transactional
    @Modifying
    @Query("update EntityPlaylist e set e.totalSongs = ?1 where e.id = ?2")
    fun updateTotalSongsById(totalSongs: Int, id: Long): Int
}
interface SongPlaylistDataSource : JpaRepository<EntitySongPlaylist, SongPlaylistId>
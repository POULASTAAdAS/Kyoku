package com.poulastaa.kyoku.activity.database.playlist.repository

import com.poulastaa.kyoku.activity.database.playlist.entity.EntityPlaylist
import com.poulastaa.kyoku.activity.database.playlist.entity.EntitySongPlaylist
import com.poulastaa.kyoku.activity.database.playlist.entity.ids.SongPlaylistId
import com.poulastaa.kyoku.activity.utils.PlaylistId
import org.springframework.data.jpa.repository.JpaRepository

interface PlaylistDatasource : JpaRepository<EntityPlaylist, PlaylistId>
interface SongPlaylistDatasource : JpaRepository<EntitySongPlaylist, SongPlaylistId>
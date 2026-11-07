package com.poulastaa.kyoku.playlist.database.playlist.repository

import com.poulastaa.kyoku.playlist.database.playlist.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.EntitySongPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.EntityUserPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.ids.SongPlaylistId
import com.poulastaa.kyoku.playlist.database.playlist.entity.ids.UserPlaylistId
import com.poulastaa.kyoku.playlist.utils.PlaylistId
import org.springframework.data.jpa.repository.JpaRepository

interface PlaylistDataSource : JpaRepository<EntityPlaylist, PlaylistId>
interface SongPlaylistDataSource : JpaRepository<EntitySongPlaylist, SongPlaylistId>
interface UserPlaylistDataSource : JpaRepository<EntityUserPlaylist, UserPlaylistId>

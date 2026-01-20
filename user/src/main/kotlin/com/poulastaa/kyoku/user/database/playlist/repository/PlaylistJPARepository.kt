package com.poulastaa.kyoku.user.database.playlist.repository

import com.poulastaa.kyoku.user.database.playlist.entity.EntityPlaylist
import com.poulastaa.kyoku.user.database.playlist.entity.EntityUserPlaylist
import com.poulastaa.kyoku.user.database.playlist.entity.ids.UserPlaylistId
import com.poulastaa.kyoku.user.utils.PlaylistId
import org.springframework.data.jpa.repository.JpaRepository

interface UserPlaylistDataSource : JpaRepository<EntityUserPlaylist, UserPlaylistId>
interface PlaylistDataSource : JpaRepository<EntityPlaylist, PlaylistId>

package com.poulastaa.kyoku.playlist.database.repository

import com.poulastaa.kyoku.playlist.database.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.utils.PlaylistId
import org.springframework.data.jpa.repository.JpaRepository

interface PlaylistDataSource : JpaRepository<EntityPlaylist, PlaylistId>
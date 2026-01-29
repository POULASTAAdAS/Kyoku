package com.poulastaa.kyoku.activity.database.content.repository

import com.poulastaa.kyoku.activity.database.content.entity.EntityArtist
import com.poulastaa.kyoku.activity.database.content.entity.EntityArtistInfo
import com.poulastaa.kyoku.activity.database.content.entity.EntitySong
import com.poulastaa.kyoku.activity.database.content.entity.EntitySongInfo
import com.poulastaa.kyoku.activity.utils.ArtistId
import com.poulastaa.kyoku.activity.utils.SongId
import org.springframework.data.jpa.repository.JpaRepository

interface SongDatasource : JpaRepository<EntitySong, SongId>
interface SongInfoDatasource : JpaRepository<EntitySongInfo, SongId>
interface ArtistDatasource : JpaRepository<EntityArtist, ArtistId>
interface ArtistInfoDatasource : JpaRepository<EntityArtistInfo, ArtistId>
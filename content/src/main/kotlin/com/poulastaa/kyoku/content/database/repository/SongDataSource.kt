package com.poulastaa.kyoku.content.database.repository

import com.poulastaa.kyoku.content.database.entity.EntitySong
import com.poulastaa.kyoku.content.utils.SongId
import org.springframework.data.jpa.repository.JpaRepository

interface SongDataSource : JpaRepository<EntitySong, SongId>

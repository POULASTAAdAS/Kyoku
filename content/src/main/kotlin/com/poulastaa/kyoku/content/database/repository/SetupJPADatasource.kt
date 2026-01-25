package com.poulastaa.kyoku.content.database.repository

import com.poulastaa.kyoku.content.database.entity.EntityGenre
import com.poulastaa.kyoku.content.utils.GenreId
import org.springframework.data.jpa.repository.JpaRepository

interface GenreDataSource : JpaRepository<EntityGenre, GenreId>
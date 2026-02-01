package com.poulastaa.kyoku.activity.database.activity.entity

import com.poulastaa.kyoku.activity.utils.GenreId
import com.poulastaa.kyoku.activity.utils.UserId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "userGenre")
data class EntityUserGenre(
    @Id
    val userId: UserId,
    val genreIds: List<GenreId>,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

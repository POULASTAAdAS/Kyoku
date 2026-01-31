package com.poulastaa.kyoku.activity.database.activity.entity

import com.poulastaa.kyoku.activity.utils.GenreId
import com.poulastaa.kyoku.activity.utils.UserId
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "userGenre")
data class UserGenre(
    @Id
    val userId: UserId,
    val genre: List<GenreId>,
)

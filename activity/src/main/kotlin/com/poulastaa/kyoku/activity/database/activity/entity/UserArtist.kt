package com.poulastaa.kyoku.activity.database.activity.entity

import com.poulastaa.kyoku.activity.utils.ArtistId
import com.poulastaa.kyoku.activity.utils.UserId
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "userArtist")
data class UserArtist(
    @Id
    val userId: UserId,
    val genre: List<ArtistId>,
)

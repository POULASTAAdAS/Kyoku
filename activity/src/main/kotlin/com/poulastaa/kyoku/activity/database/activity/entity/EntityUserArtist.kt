package com.poulastaa.kyoku.activity.database.activity.entity

import com.poulastaa.kyoku.activity.utils.ArtistId
import com.poulastaa.kyoku.activity.utils.UserId
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collation = "userArtist")
data class EntityUserArtist(
    @Id
    val userId: UserId,
    val artistIds: List<ArtistId>,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

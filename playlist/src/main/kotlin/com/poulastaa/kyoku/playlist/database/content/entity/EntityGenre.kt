package com.poulastaa.kyoku.playlist.database.content.entity

import com.poulastaa.kyoku.playlist.database.playlist.entity.BaseIdEntity
import com.poulastaa.kyoku.playlist.utils.GenreId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "Genre")
class EntityGenre(
    @Column(name = "name", nullable = false, length = 60, unique = true)
    val name: String,

    @Column(name = "cover_image", nullable = true, length = 200)
    val coverImage: String?,

    @Column(name = "popularity", nullable = false, columnDefinition = "BIGINT UNSIGNED DEFAULT 0")
    val popularity: Long = 0,
) : BaseIdEntity<GenreId>()

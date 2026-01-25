package com.poulastaa.kyoku.content.database.entity

import com.poulastaa.kyoku.content.database.BaseIdEntity
import com.poulastaa.kyoku.content.utils.AlbumId
import jakarta.persistence.*

@Entity
@Table(name = "Album")
class EntityAlbum(
    @Column(name = "name", nullable = false, length = 100, unique = true)
    val name: String,

    @Column(name = "popularity", nullable = false, columnDefinition = "BIGINT UNSIGNED DEFAULT 0")
    val popularity: Long = 0,

    @ManyToMany(mappedBy = "albums", fetch = FetchType.LAZY)
    val artists: MutableSet<EntityArtist> = mutableSetOf(),
) : BaseIdEntity<AlbumId>()

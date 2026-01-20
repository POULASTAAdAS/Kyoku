package com.poulastaa.kyoku.playlist.database.content.entity

import com.poulastaa.kyoku.playlist.database.playlist.entity.BaseIdEntity
import com.poulastaa.kyoku.playlist.utils.ArtistId
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "ArtistInfo")
class EntityArtistInfo(
    @Column(name = "biography", nullable = true, columnDefinition = "TEXT")
    val biography: String? = null,

    @Column(name = "birth_date", nullable = true)
    @Temporal(TemporalType.DATE)
    val birthDate: Date? = null,

    @Column(name = "monthly_listeners", nullable = false, columnDefinition = "BIGINT UNSIGNED DEFAULT 0")
    val monthlyListeners: Long = 0,

    @OneToOne
    @MapsId
    @JoinColumn(name = "artist_id")
    val artist: EntityArtist,
) : BaseIdEntity<ArtistId>()

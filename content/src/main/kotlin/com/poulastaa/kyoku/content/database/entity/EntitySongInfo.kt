package com.poulastaa.kyoku.content.database.entity

import com.poulastaa.kyoku.content.database.BaseIdEntity
import com.poulastaa.kyoku.content.utils.SongId
import jakarta.persistence.*

@Entity
@Table(name = "SongInfo")
class EntitySongInfo(
    @Column(name = "release_year", nullable = false)
    val releaseYear: Int,

    @Column(name = "composer", nullable = true, length = 200)
    val composer: String? = null,

    @Column(name = "popularity", nullable = false, columnDefinition = "BIGINT UNSIGNED DEFAULT 0")
    var popularity: Long = 0,

    @OneToOne
    @MapsId
    @JoinColumn(name = "song_id")
    val song: EntitySong,
) : BaseIdEntity<SongId>()

package com.poulastaa.kyoku.playlist.database.entity

import com.poulastaa.kyoku.playlist.utils.SongId
import jakarta.persistence.*

@Entity
@Table(name = "song")
class EntitySong(
    @Column(name = "title", nullable = false, length = 150)
    val title: String,
    @Column(name = "poster", nullable = true, length = 300)
    val poster: String?,
    @Column(name = "master_playlist", nullable = false, length = 300)
    val url: String,
) : BaseIdEntity<SongId>() {
    @OneToOne(mappedBy = "song", cascade = [CascadeType.ALL], optional = true)
    var songInfo: EntitySongInfo? = null
}
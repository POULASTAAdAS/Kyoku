package com.poulastaa.kyoku.content.database.entity

import com.poulastaa.kyoku.content.database.BaseIdEntity
import com.poulastaa.kyoku.content.utils.SongId
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
    @Transient
    var songInfo: EntitySongInfo? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SongArtist",
        joinColumns = [JoinColumn(name = "song_id")],
        inverseJoinColumns = [JoinColumn(name = "artist_id")]
    )
    val artists: MutableSet<EntityArtist> = mutableSetOf()

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SongAlbum",
        joinColumns = [JoinColumn(name = "song_id")],
        inverseJoinColumns = [JoinColumn(name = "album_id")]
    )
    val albums: MutableSet<EntityAlbum> = mutableSetOf()

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SongGenre",
        joinColumns = [JoinColumn(name = "song_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: MutableSet<EntityGenre> = mutableSetOf()

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SongCountry",
        joinColumns = [JoinColumn(name = "song_id")],
        inverseJoinColumns = [JoinColumn(name = "country_id")]
    )
    val countries: MutableSet<EntityCountry> = mutableSetOf()
}
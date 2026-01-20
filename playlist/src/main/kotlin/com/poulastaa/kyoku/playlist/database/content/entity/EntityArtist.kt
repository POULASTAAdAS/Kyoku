package com.poulastaa.kyoku.playlist.database.content.entity

import com.poulastaa.kyoku.playlist.database.playlist.entity.BaseIdEntity
import com.poulastaa.kyoku.playlist.utils.ArtistId
import jakarta.persistence.*

@Entity
@Table(name = "Artist")
class EntityArtist(
    @Column(name = "name", nullable = false, length = 100, unique = true)
    val name: String,

    @Column(name = "cover_image", nullable = true, length = 200)
    val coverImage: String?,

    @Column(name = "followers", nullable = false, columnDefinition = "BIGINT UNSIGNED DEFAULT 0")
    val followers: Long = 0,

    @Transient
    var artistInfo: EntityArtistInfo? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ArtistAlbum",
        joinColumns = [JoinColumn(name = "artist_id")],
        inverseJoinColumns = [JoinColumn(name = "album_id")]
    )
    val albums: MutableSet<EntityAlbum> = mutableSetOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ArtistGenre",
        joinColumns = [JoinColumn(name = "artist_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: MutableSet<EntityGenre> = mutableSetOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ArtistCountry",
        joinColumns = [JoinColumn(name = "artist_id")],
        inverseJoinColumns = [JoinColumn(name = "country_id")]
    )
    val countries: MutableSet<EntityCountry> = mutableSetOf(),
) : BaseIdEntity<ArtistId>()

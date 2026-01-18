package com.poulastaa.kyoku.playlist.database.playlist.entity

import com.poulastaa.kyoku.playlist.database.playlist.entity.ids.SongPlaylistId
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy

@Entity
@Table(name = "SongPlaylist")
class EntitySongPlaylist(
    @EmbeddedId
    val id: SongPlaylistId = SongPlaylistId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playlistId")
    @JoinColumn(
        name = "playlist_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_song_playlist",
            foreignKeyDefinition = "FOREIGN KEY (`playlist_id`) REFERENCES `Playlist`(`id`) ON DELETE CASCADE"
        )
    )
    var playlist: EntityPlaylist? = null,
) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass

        if (thisEffectiveClass != oEffectiveClass) return false
        other as EntitySongPlaylist

        return id == other.id
    }

    final override fun hashCode() = if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode()
    else javaClass.hashCode()
}

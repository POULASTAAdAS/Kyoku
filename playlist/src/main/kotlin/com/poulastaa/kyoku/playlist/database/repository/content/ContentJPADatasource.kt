package com.poulastaa.kyoku.playlist.database.repository.content

import com.poulastaa.kyoku.playlist.database.content.entity.EntityArtistInfo
import com.poulastaa.kyoku.playlist.database.content.entity.EntitySong
import com.poulastaa.kyoku.playlist.database.content.entity.EntitySongInfo
import com.poulastaa.kyoku.playlist.utils.ArtistId
import com.poulastaa.kyoku.playlist.utils.SongId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SongDataSource : JpaRepository<EntitySong, SongId> {
    @Query(
        value = """
            SELECT DISTINCT s.id
            FROM song s
            WHERE LOWER(s.title) REGEXP :pattern
            AND s.title NOT REGEXP '(Remix|Mashup|LoFi|Slowed|New Years|Reverb|8D|Nightcore)'
        """,
        nativeQuery = true
    )
    fun findSongIdsBySimilarTitles(@Param("pattern") pattern: String): List<Long>

    @Query(
        """
            SELECT DISTINCT s FROM EntitySong s
            WHERE s.id IN :ids
        """
    )
    fun findSongsByIds(@Param("ids") ids: List<Long>): List<EntitySong>

    @Query(
        """
            SELECT DISTINCT s FROM EntitySong s
            LEFT JOIN FETCH s.artists
            WHERE s.id IN :ids
        """
    )
    fun findSongsWithArtistsByIds(@Param("ids") ids: List<Long>): List<EntitySong>

    @Query(
        """
            SELECT DISTINCT s FROM EntitySong s
            LEFT JOIN FETCH s.albums
            WHERE s.id IN :ids
        """
    )
    fun findSongsWithAlbumsByIds(@Param("ids") ids: List<Long>): List<EntitySong>

    @Query(
        """
            SELECT DISTINCT s FROM EntitySong s
            LEFT JOIN FETCH s.genres
            WHERE s.id IN :ids
        """
    )
    fun findSongsWithGenresByIds(@Param("ids") ids: List<Long>): List<EntitySong>

    @Query(
        """
            SELECT DISTINCT s FROM EntitySong s
            LEFT JOIN FETCH s.countries
            WHERE s.id IN :ids
        """
    )
    fun findSongsWithCountriesByIds(@Param("ids") ids: List<Long>): List<EntitySong>
}

interface SongInfoDataSource : JpaRepository<EntitySongInfo, SongId> {
    @Query("SELECT si FROM EntitySongInfo si WHERE si.id IN :ids")
    fun findAllByIds(@Param("ids") ids: List<Long>): List<EntitySongInfo>
}

interface ArtistInfoDataSource : JpaRepository<EntityArtistInfo, ArtistId> {
    @Query("SELECT ai FROM EntityArtistInfo ai WHERE ai.id IN :ids")
    fun findAllByIds(@Param("ids") ids: List<Long>): List<EntityArtistInfo>
}
package com.poulastaa.kyoku.playlist.database.repository

import com.poulastaa.kyoku.playlist.database.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.database.entity.EntitySong
import com.poulastaa.kyoku.playlist.utils.PlaylistId
import com.poulastaa.kyoku.playlist.utils.SongId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PlaylistDataSource : JpaRepository<EntityPlaylist, PlaylistId>
interface SongDataSource : JpaRepository<EntitySong, SongId> {
    @Query(
        value = """
            SELECT * FROM song 
            WHERE LOWER(title) REGEXP :pattern
            AND song.title NOT REGEXP '(Remix|Mashup|LoFi|Slowed|New Years)'
        """,
        nativeQuery = true
    )
    fun findBySimilarTitles(@Param("pattern") pattern: String): List<EntitySong>
}
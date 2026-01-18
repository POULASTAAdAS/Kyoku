package com.poulastaa.kyoku.playlist.database.repository.content

import com.poulastaa.kyoku.playlist.database.content.entity.EntitySong
import com.poulastaa.kyoku.playlist.utils.SongId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SongDataSource : JpaRepository<EntitySong, SongId> {
    @Query(
        value = """
            SELECT s.*, si.* 
            FROM song s
            LEFT JOIN SongInfo si ON s.id = si.song_id
            WHERE LOWER(s.title) REGEXP :pattern
            AND s.title NOT REGEXP '(Remix|Mashup|LoFi|Slowed|New Years|Reverb|8D|Nightcore)'
        """,
        nativeQuery = true
    )
    fun findBySimilarTitles(@Param("pattern") pattern: String): List<EntitySong>
}
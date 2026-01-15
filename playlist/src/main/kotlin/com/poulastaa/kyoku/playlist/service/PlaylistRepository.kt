package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.playlist.database.entity.EntitySong
import com.poulastaa.kyoku.playlist.database.repository.SongDataSource
import com.poulastaa.kyoku.playlist.domain.model.DtoSong
import com.poulastaa.kyoku.playlist.utils.SpotifySongTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.regex.Pattern

@Service
class PlaylistRepository(
    private val db: SongDataSource,
    private val cache: RedisCacheService,
) {
    @Transactional(readOnly = true)
    suspend fun getSongByTitles(titles: List<SpotifySongTitle>): List<DtoSong> = coroutineScope {
        if (titles.isEmpty()) return@coroutineScope emptyList()

        val cachedSongsByTitle = cache.cacheSongByTitle(titles)
        val uncachedTitles = titles.filter { it !in cachedSongsByTitle.keys }

        val dbSongs = if (uncachedTitles.isNotEmpty()) async(Dispatchers.IO) {
            val pattern = titles.joinToString("|") { Pattern.quote(it.lowercase()) }
            db.findBySimilarTitles(pattern).map { it.toDtoSong() }.also {
                cache.setSongById(it)
                cache.setSongByTitle(it)
            }
        }.await() else emptyList()

        cachedSongsByTitle.values + dbSongs
    }

    private fun EntitySong.toDtoSong(): DtoSong = TODO("implement mapper for EntitySong to DtoSong")
}
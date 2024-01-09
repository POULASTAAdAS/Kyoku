package com.example.data.repository.song_db

import com.example.data.model.*
import com.example.data.model.database_table.SongTable
import com.example.domain.dao.Song
import com.example.domain.repository.song_db.SongRepository
import com.example.plugins.dbQuery
import com.example.util.toResponseSong
import io.ktor.util.collections.*
import org.jetbrains.exposed.sql.and
import java.io.File

class SongRepositoryImpl : SongRepository {
    override suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist {
        val hasMapOfFoundSongs = ConcurrentMap<Long, ResponseSong>()

        return try {
            dbQuery {
                list.forEach { spotifySong ->
                    Song.find {
                        SongTable.title like "%${spotifySong.title}%" and (SongTable.album like "%${spotifySong.album}%")
                    }.forEach {
                        if (!hasMapOfFoundSongs.containsKey(it.id.value))
                            hasMapOfFoundSongs[it.id.value] = it.toResponseSong()
                    }

                    Song.find {
                        SongTable.title like "%${spotifySong.title}%"
                    }.forEach {
                        if (!hasMapOfFoundSongs.containsKey(it.id.value))
                            if (spotifySong.album!!.contains(it.album))
                                hasMapOfFoundSongs[it.id.value] = it.toResponseSong()

                        println(it.toResponseSong())
                    }
                }
            }

            HandleSpotifyPlaylist(
                status = HandleSpotifyPlaylistStatus.SUCCESS,
                spotifyPlaylistResponse = SpotifyPlaylistResponse(
                    listOfResponseSong = hasMapOfFoundSongs.values.toList()
                ),
                spotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(
                    listOfSong = notFoundSongs(list, hasMapOfFoundSongs.values.toList())
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            HandleSpotifyPlaylist(
                status = HandleSpotifyPlaylistStatus.FAILURE
            )
        }
    }

    override fun getCoverImage(path: String): File? = try {
        File(path)
    } catch (e: Exception) {
        null
    }
}

private fun notFoundSongs(
    list: List<SpotifySong>,
    responseSong: List<ResponseSong>
): List<SpotifySong> {
    val listOfNotFoundSongs = ArrayList<SpotifySong>()

    for (spotifySong in list) {
        var found = false
        for (res in responseSong) {
            if (res.title.contains(spotifySong.title!!)) {
                found = true
                break
            }
        }

        if (!found)
            listOfNotFoundSongs.add(spotifySong)
    }

    for (res in responseSong) {
        for (spotifySong in listOfNotFoundSongs) {
            if (res.title.contains(spotifySong.title!!))
                listOfNotFoundSongs.remove(spotifySong)
        }
    }

    return listOfNotFoundSongs
}
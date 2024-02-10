package com.poulastaa.data.repository

import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.spotify.*
import com.poulastaa.domain.dao.Song
import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toResponseSongList
import io.ktor.util.collections.*
import org.jetbrains.exposed.sql.and
import java.io.File

class SongRepositoryImpl : SongRepository {
    override suspend fun handleSpotifyPlaylist(list: List<SpotifySong>): HandleSpotifyPlaylist {
        val hasMapOfFoundSongs = ConcurrentMap<Long, Song>()

        return try {
            dbQuery {
                list.forEach { spotifySong ->
                    Song.find { // searching by title and album
                        SongTable.title like "%${spotifySong.title}%" and (SongTable.album like "%${spotifySong.album}%")
                    }.forEach {
                        if (!hasMapOfFoundSongs.containsKey(it.id.value))
                            hasMapOfFoundSongs[it.id.value] = it
                    }

                    Song.find {// searching by title
                        SongTable.title like "%${spotifySong.title}%"
                    }.forEach {
                        if (!hasMapOfFoundSongs.containsKey(it.id.value)) // putting if any left
                            if (spotifySong.album!!.contains(it.album))
                                hasMapOfFoundSongs[it.id.value] = it
                    }
                }
            }

            HandleSpotifyPlaylist(
                status = HandleSpotifyPlaylistStatus.SUCCESS,
                spotifyPlaylistResponse = SpotifyPlaylistResponse(
                    status = HandleSpotifyPlaylistStatus.SUCCESS,
                    listOfResponseSong = hasMapOfFoundSongs.values.toResponseSongList()
                ),
                spotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(
                    listOfSong = notFoundSongs(list, hasMapOfFoundSongs.values.toResponseSongList())
                ),
                songIdList = hasMapOfFoundSongs.keys.toList()
            )
        } catch (e: Exception) {
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
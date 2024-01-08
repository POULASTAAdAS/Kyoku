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

        val listOfNoteFoundSongs = ArrayList<SpotifySong>()

        return try {
            dbQuery {
                list.forEach { spotifySong ->
                    dbQuery {
                        Song.find {
                            SongTable.title like "%${spotifySong.title}%" and (SongTable.album like "%${spotifySong.album}%")
                        }
                    }.forEach {
                        if (!hasMapOfFoundSongs.containsKey(it.id.value))
                            hasMapOfFoundSongs[it.id.value] = it.toResponseSong()
                    }

                    dbQuery {
                        Song.find {
                            SongTable.title like "%${spotifySong.title}%"
                        }.forEach {
                            if (!hasMapOfFoundSongs.containsKey(it.id.value))
                                if (spotifySong.title!!.contains(it.album))
                                    hasMapOfFoundSongs[it.id.value] = it.toResponseSong()
                        }
                    }
                }
            }

            // todo construct not found song list

            HandleSpotifyPlaylist(
                status = HandleSpotifyPlaylistStatus.SUCCESS,
                spotifyPlaylistResponse = SpotifyPlaylistResponse(
                    listOfResponseSong = hasMapOfFoundSongs.values.toList()
                ),
                spotifySongDownloaderApiReq = SpotifySongDownloaderApiReq(
                    listOfSong = listOfNoteFoundSongs
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
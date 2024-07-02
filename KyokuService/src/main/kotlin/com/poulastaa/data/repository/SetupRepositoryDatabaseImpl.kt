package com.poulastaa.data.repository

import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.GenreDao
import com.poulastaa.data.dao.PlaylistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.mappers.toArtistDto
import com.poulastaa.data.mappers.toGenreDto
import com.poulastaa.data.mappers.toResultSong
import com.poulastaa.data.model.ArtistDto
import com.poulastaa.data.model.GenreDto
import com.poulastaa.domain.model.PlaylistResult
import com.poulastaa.domain.model.SongWithArtistResult
import com.poulastaa.domain.repository.DatabaseRepository
import com.poulastaa.domain.repository.SetupRepository
import com.poulastaa.domain.repository.SpotifySongTitle
import com.poulastaa.domain.table.ArtistTable
import com.poulastaa.domain.table.GenreTable
import com.poulastaa.domain.table.PlaylistTable
import com.poulastaa.domain.table.SongTable
import com.poulastaa.plugins.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.not

class SetupRepositoryDatabaseImpl(
    private val kyokuDatabase: DatabaseRepository,
) : SetupRepository {
    override suspend fun getSpotifyPlaylist(
        spotifyPayload: List<SpotifySongTitle>,
    ): PlaylistResult = coroutineScope {
        val resultDef = spotifyPayload.map {
            async(Dispatchers.IO) {
                query {
                    SongDao.find {
                        SongTable.title like "$it%" and
                                (not(SongTable.title like "%Remix%")) and
                                (not(SongTable.title like "%Mashup%")) and
                                (not(SongTable.title like "%LoFi%")) and
                                (not(SongTable.title like "%New Years%")) and
                                (not(SongTable.title like "%Slowed%"))
                    }.groupBy {
                        it.title
                    }.map {
                        it.value.first()
                    }
                }
            }
        }

        val playlistDef = async(Dispatchers.IO) {
            query {
                PlaylistDao.new {
                    this.name = "Playlist"
                }
            }
        }

        val songDaos = query {
            resultDef.awaitAll().flatten()
        }
        val playlistDao = playlistDef.await()

        val updatePlaylistDef = async(Dispatchers.IO) {
            query {
                playlistDao.name = "Playlist ${playlistDao.id.value}"

                PlaylistDao.find {
                    PlaylistTable.id eq playlistDao.id
                }.single()
            }
        }

        val songIds = songDaos.map { it.id.value }
        val resultSongsIdList = query {
            kyokuDatabase.getArtistOnSongIdList(songIds)
        }

        val resultSongList = songDaos.map { song ->
            song.toResultSong()
        }

        val playlist = updatePlaylistDef.await()

        PlaylistResult(
            id = playlist.id.value,
            name = playlist.name,
            listOfSong = resultSongList.map {
                SongWithArtistResult(
                    resultSong = it,
                    artistList = resultSongsIdList.mapNotNull { pair ->
                        if (pair.first == it.id) pair.second else null
                    }.flatten()
                )
            }
        )
    }

    override suspend fun getGenre(sentGenreIdList: List<Int>): List<GenreDto> = query {
        GenreDao.find {
            GenreTable.id notInList sentGenreIdList
        }.orderBy(GenreTable.points to SortOrder.DESC)
            .limit(if (sentGenreIdList.isEmpty()) 7 else 4).toList()
    }.map {
        it.toGenreDto()
    }

    override suspend fun getArtist(sentArtistIdList: List<Long>): List<ArtistDto> = query {
        ArtistDao.find {
            ArtistTable.id notInList sentArtistIdList
        }.orderBy(ArtistTable.points to SortOrder.DESC)
            .limit(if (sentArtistIdList.isEmpty()) 7 else 4).toList()
            .map {
                it.toArtistDto()
            }
    }
}
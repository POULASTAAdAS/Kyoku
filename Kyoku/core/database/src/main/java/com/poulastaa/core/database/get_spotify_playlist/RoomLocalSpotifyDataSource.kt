package com.poulastaa.core.database.get_spotify_playlist

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.GetSpotifyPlaylistDao
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.mapper.toPlaylistEntity
import com.poulastaa.core.database.mapper.toPlaylistSong
import com.poulastaa.core.database.mapper.toSongsEntity
import com.poulastaa.core.domain.get_spotify_playlist.LocalSpotifyDataSource
import com.poulastaa.core.domain.get_spotify_playlist.playlistId
import com.poulastaa.core.domain.get_spotify_playlist.songId
import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.PlaylistWithSongInfo
import com.poulastaa.core.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalSpotifyDataSource @Inject constructor(
    private val commonDao: CommonDao,
    private val spotifyDao: GetSpotifyPlaylistDao,
) : LocalSpotifyDataSource {
    override suspend fun insertSongs(songs: List<Song>): List<songId> = try {
        val entrys = songs.toSongsEntity()

        commonDao.insertSongs(entrys)
        entrys.map { it.id }
    } catch (e: Exception) {
        emptyList()
    }

    override suspend fun insertPlaylist(playlist: Playlist): playlistId = try {
        val entry = playlist.toPlaylistEntity()

        commonDao.insertPlaylist(entry)
        entry.id
    } catch (e: Exception) {
        -1L
    }

    override suspend fun createRelationOnSongAndPlaylist(
        songIdList: List<songId>,
        playlistId: playlistId,
    ) {
        val entrys = songIdList.map {
            SongPlaylistRelationEntity(it, playlistId)
        }

        commonDao.insertSongPlaylistRelation(entrys)
    }

    override suspend fun getSongOnUrl(url: String): songId? = commonDao.getSongOnUrl(url)

    override suspend fun addColorToSong(
        songId: songId,
        encodedCoverImage: String,
        primary: String,
        background: String,
        onBackground: String,
    ) {

    }

    override fun getAllPlaylistWithSong(): Flow<List<PlaylistWithSongInfo>> =
        spotifyDao.getAllPlaylist().map { rawRes ->
            rawRes.groupBy { group ->
                group.playlistId
            }.map { map ->
                PlaylistWithSongInfo(
                    playlistId = map.key,
                    name = map.value.first().playlistName,
                    listOfPlaylistSong = map.value.map { song ->
                        song.toPlaylistSong()
                    }
                )
            }
        }
}
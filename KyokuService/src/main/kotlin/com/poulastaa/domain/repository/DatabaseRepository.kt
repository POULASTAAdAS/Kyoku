package com.poulastaa.domain.repository

import com.poulastaa.data.dao.AlbumDao
import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.PlaylistDao
import com.poulastaa.data.model.SongDto
import com.poulastaa.domain.model.ResultArtist
import com.poulastaa.domain.model.UserType

interface DatabaseRepository {
    fun updateSongPointByOne(list: List<Long>)
    fun updateArtistPointByOne(list: List<Long>)

    suspend fun getArtistOnSongId(songId: Long): List<ResultArtist>
    suspend fun getArtistOnSongIdList(list: List<Long>): List<Pair<Long, List<ResultArtist>>>

    fun updateGenrePointByOne(list: List<Int>)

    suspend fun getSongOnId(
        id: Long,
    ): SongDto

    suspend fun createPlaylist(
        name: String,
        userId: Long,
        userType: UserType,
        songIdList: List<Long>,
    ): Long

    suspend fun getPlaylistOnId(id: Long): PlaylistDao?
    suspend fun getAlbumOnId(albumId: Long): AlbumDao?
    suspend fun getArtistOnId(artistId: Long): ArtistDao?
}
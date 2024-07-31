package com.poulastaa.domain.repository

import com.poulastaa.data.model.SongDto
import com.poulastaa.domain.model.ResultArtist

interface DatabaseRepository {
    fun updateSongPointByOne(list: List<Long>)
    fun updateArtistPointByOne(list: List<Long>)

    suspend fun getArtistOnSongId(songId: Long): List<ResultArtist>
    suspend fun getArtistOnSongIdList(list: List<Long>): List<Pair<Long, List<ResultArtist>>>

    fun updateGenrePointByOne(list: List<Int>)

    suspend fun getSongOnId(
        id:Long
    ): SongDto
}
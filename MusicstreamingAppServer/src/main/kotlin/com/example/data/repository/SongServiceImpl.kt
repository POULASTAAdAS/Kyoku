package com.example.data.repository

import com.example.domain.model.SongTable
import com.example.domain.repository.SongService
import com.example.plugins.dbQuery
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.select

class SongServiceImpl : SongService {

    override suspend fun readSong(): String {
        var path = ""

        dbQuery {
            SongTable.select {
                SongTable.id eq 803
            }.map {
                path = it[SongTable.masterPlaylistPath]
            }
        }
        return path
    }
}
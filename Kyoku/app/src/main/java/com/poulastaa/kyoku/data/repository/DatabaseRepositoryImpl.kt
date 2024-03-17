package com.poulastaa.kyoku.data.repository

import com.poulastaa.kyoku.data.database.AppDao
import com.poulastaa.kyoku.data.model.api.service.home.AlbumPreview
import com.poulastaa.kyoku.data.model.api.service.home.DailyMixPreview
import com.poulastaa.kyoku.data.model.api.service.home.FevArtistsMixPreview
import com.poulastaa.kyoku.data.model.api.service.home.ResponseArtistsPreview
import com.poulastaa.kyoku.data.model.database.PlaylistWithSongs
import com.poulastaa.kyoku.data.model.database.table.AlbumPreviewSongRelationTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPreviewSongRelation
import com.poulastaa.kyoku.data.model.database.table.DailyMixPrevTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongTable
import com.poulastaa.kyoku.data.model.screens.home.HomeAlbumUiPrev
import com.poulastaa.kyoku.utils.toAlbumTableEntry
import com.poulastaa.kyoku.utils.toArtistTableEntry
import com.poulastaa.kyoku.utils.toFevArtistMixPrevTable
import com.poulastaa.kyoku.utils.toSongPrev
import com.poulastaa.kyoku.utils.toSongPrevTableEntry
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
class DatabaseRepositoryImpl @Inject constructor(
    private val dao: AppDao
) {
    suspend fun insertSong(song: SongTable): Long = dao.insertSong(song)

    suspend fun insertPlaylist(name: String) = dao.insertPlaylist(
        playlist = PlaylistTable(
            name = name
        )
    )

    suspend fun insertSongPlaylistRelation(data: SongPlaylistRelationTable) =
        dao.insertSongPlaylistRelation(data)

    fun getAllPlaylist(): Flow<List<PlaylistWithSongs>> = dao.getAllPlaylist()

    fun getAllSong(): Flow<List<SongTable>> = dao.getAllSong()

    suspend fun checkIfNewUser() = dao.checkIfNewUser().isEmpty()

    fun insertIntoFevArtistMixPrev(list: List<FevArtistsMixPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.forEach {
                dao.insertIntoFevArtistMixPrev(
                    data = it.toFevArtistMixPrevTable()
                )
            }
        }
    }

    fun insertIntoAlbum(list: List<AlbumPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.forEach {
                val albumId = dao.insertIntoAlbum(data = it.toAlbumTableEntry())

                it.listOfSongs.forEach { song ->
                    val songId = dao.insertIntoSongPrev(data = song.toSongPrevTableEntry())

                    dao.insertIntoAlbumPrevSongRelationTable(
                        data = AlbumPreviewSongRelationTable(
                            albumId = albumId,
                            songId = songId
                        )
                    )
                }
            }
        }
    }

    fun insertResponseArtistPrev(list: List<ResponseArtistsPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.forEach {
                val artistId = dao.insertIntoArtist(it.artist.toArtistTableEntry())
                it.listOfSongs.forEach { previewSong ->
                    val songId = dao.insertIntoSongPrev(previewSong.toSongPrevTableEntry())

                    dao.insertIntoArtistPrevSongRelationTable(
                        data = ArtistPreviewSongRelation(
                            artistId = artistId.toInt(),
                            songId = songId
                        )
                    )
                }
            }
        }
    }

    fun insertDailyMixPrev(data: DailyMixPreview) {
        CoroutineScope(Dispatchers.IO).launch {
            data.listOfSongs.forEach {
                dao.insertIntoDailyMixPrevTable(
                    data = DailyMixPrevTable(
                        id = dao.insertIntoSongPrev(
                            data = it.toSongPrevTableEntry()
                        )
                    )
                )
            }
        }
    }

    suspend fun readAllAlbumPrev() = dao.readAllAlbumPrev().groupBy {
        it.name
    }.map {
        HomeAlbumUiPrev(
            name = it.key,
            listOfSong = it.value.map { song ->
                song.toSongPrev()
            }
        )
    }
}
package com.poulastaa.kyoku.data.repository

import android.content.Context
import com.poulastaa.kyoku.data.database.AppDao
import com.poulastaa.kyoku.data.database.InternalDao
import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import com.poulastaa.kyoku.data.model.api.service.home.AlbumPreview
import com.poulastaa.kyoku.data.model.api.service.home.DailyMixPreview
import com.poulastaa.kyoku.data.model.api.service.home.FevArtistsMixPreview
import com.poulastaa.kyoku.data.model.api.service.home.Pinned
import com.poulastaa.kyoku.data.model.api.service.home.ResponseAlbum
import com.poulastaa.kyoku.data.model.api.service.home.ResponseArtistsPreview
import com.poulastaa.kyoku.data.model.api.service.home.ResponsePlaylist
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import com.poulastaa.kyoku.data.model.api.service.pinned.IdType
import com.poulastaa.kyoku.data.model.api.service.pinned.PinnedOperation
import com.poulastaa.kyoku.data.model.database.table.AlbumTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPreviewSongRelation
import com.poulastaa.kyoku.data.model.database.table.FevArtistOrDailyMixPreviewTable
import com.poulastaa.kyoku.data.model.database.table.MixType
import com.poulastaa.kyoku.data.model.database.table.PinnedTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistSongTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.SongAlbumRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalItemTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalPinnedTable
import com.poulastaa.kyoku.data.model.screens.library.PinnedDataType
import com.poulastaa.kyoku.data.model.screens.song_view.UiAlbum
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.toAlbumTableEntry
import com.poulastaa.kyoku.utils.toAlbumTablePrevEntry
import com.poulastaa.kyoku.utils.toArtistMixEntry
import com.poulastaa.kyoku.utils.toArtistSongEntry
import com.poulastaa.kyoku.utils.toArtistTableEntry
import com.poulastaa.kyoku.utils.toDailyMixEntry
import com.poulastaa.kyoku.utils.toFavouriteTableEntry
import com.poulastaa.kyoku.utils.toHistoryPrevSongEntry
import com.poulastaa.kyoku.utils.toPlaylistSongTable
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class DatabaseRepositoryImpl @Inject constructor(
    private val dao: AppDao,
    private val intDao: InternalDao
) {
    private var context: Context? = null
    private var header: String? = null

    fun setValues(context: Context, header: String) {
        this.context = context
        this.header = header
    }

    fun insertIntoPlaylist(
        data: List<ResponseSong>,
        id: Long,
        playlistName: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val playlistId = async {
                dao.insertPlaylist(
                    playlist = PlaylistTable(
                        playlistId = id,
                        name = playlistName
                    )
                )
            }.await()

            async {
                dao.insertIntoPlaylistSongTable(
                    entrys = data.map {
                        it.toPlaylistSongTable()
                    }
                )
            }.await()

            async {
                dao.insertIntoSongPlaylistRelation(
                    entrys = data.map {
                        it.id
                    }.map {
                        SongPlaylistRelationTable(
                            playlistId = playlistId,
                            songId = it
                        )
                    }
                )
            }.await()
        }
    }

    fun getAllPlaylist() = dao.getAllPlaylist()


    suspend fun isFirstOpen() = dao.isFirstOpen().isEmpty()

    suspend fun checkIfNewUser() = withContext(Dispatchers.IO) {
        val a = async { dao.ifNewUserCheck_1().isEmpty() }

        val b = async { dao.ifNewUserCheck_2().isEmpty() }

        val c = async { dao.isNewUserCheck_3().isEmpty() }

        a.await() && b.await() && c.await()
    }

    fun insertIntoFevArtistMixPrev(list: List<FevArtistsMixPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            async {
                list.map {
                    FevArtistOrDailyMixPreviewTable(
                        artist = it.artist,
                        coverImage = it.coverImage,
                        type = MixType.ARTIST_MIX
                    )
                }.let {
                    dao.insertIntoFevArtistOrDailyMixPrev(
                        entrys = it
                    )
                }
            }.await()
        }
    }


    fun insertDailyMixPrev(data: DailyMixPreview) {
        CoroutineScope(Dispatchers.IO).launch {
            data.listOfSongs.map {
                FevArtistOrDailyMixPreviewTable(
                    artist = it.artist,
                    coverImage = it.coverImage,
                    type = MixType.DAILY_MIX
                )
            }.let {
                dao.insertIntoFevArtistOrDailyMixPrev(
                    entrys = it
                )
            }
        }
    }

    fun insertIntoAlbumPrev(list: List<AlbumPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertIntoPrevAlbum(entrys = list.toAlbumTablePrevEntry())
        }
    }

    fun insertResponseArtistPrev(list: List<ResponseArtistsPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.forEach { entry ->
                dao.insertIntoArtist(
                    entry = entry.artist.toArtistTableEntry()
                )?.let { artistId ->
                    if (artistId != -1L) // bal r bug
                        entry.listOfSongs.forEach { song ->
                            val songId = async {
                                dao.insertIntoArtistSong(entry = song.toArtistSongEntry())
                            }.await()

                            songId?.let {
                                dao.insertIntoArtistPrevSongRelationTable(
                                    data = ArtistPreviewSongRelation(
                                        artistId = artistId,
                                        songId = songId
                                    )
                                )
                            }
                        }
                }
            }
        }
    }

    fun insertIntoPinned(list: List<Pinned>) {
        if (list.isEmpty()) return

        CoroutineScope(Dispatchers.IO).launch {
            list.forEach { pinned ->
                when (pinned.type) {
                    IdType.PLAYLIST -> {
                        dao.getPlaylistInternalId(pinned.id)?.let { id ->
                            dao.addToPinnedTable(
                                data = PinnedTable(
                                    playlistId = id
                                )
                            )
                        }
                    }

                    IdType.ALBUM -> {
                        dao.getAlbumInternalId(pinned.id)?.let { id ->
                            dao.addToPinnedTable(
                                data = PinnedTable(
                                    albumId = id
                                )
                            )
                        }
                    }

                    IdType.ARTIST -> {
                        dao.getArtistInternalId(pinned.id.toInt())?.let { id ->
                            dao.addToPinnedTable(
                                data = PinnedTable(
                                    artistId = id
                                )
                            )
                        }
                    }

                    else -> Unit
                }
            }
        }
    }


    suspend fun readFevArtistMixPrev() = dao.readFevArtistMixPrev()
    suspend fun readDailyMixPrevUrls() = dao.readDailyMixPrevUrls()

    fun readAllAlbumPrev() = dao.readAllAlbumPrev()
    fun readAllArtistPrev() = dao.readAllArtistPrev()
    fun readPlaylistPreview() = dao.readPreviewPlaylist()
    fun redRecentlyPlayed() = dao.redRecentlyPlayed()

    suspend fun isInFavourite(id: Long) = dao.isInFavourite(id)?.let { true } ?: false
    fun countFavouriteSong() = dao.countFavouriteSong()

    fun radSavedAlbumPrev() = dao.radSavedAlbumPrev()
    suspend fun readFavouritePrev() = dao.readFavouritePrev()


    fun insertIntoPlaylist(list: List<ResponsePlaylist>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.forEach {
                insertIntoPlaylist(
                    data = it.listOfSongs,
                    id = it.id,
                    playlistName = it.name
                )
            }
        }
    }

    fun insertIntoFavourite(list: List<ResponseSong>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertIntoFavourite(
                entrys = list.toFavouriteTableEntry()
            )
        }
    }

    suspend fun checkIfSongAlreadyInFavourite(songId: Long) = false
//        dao.getAllFavouriteSongId().firstOrNull {
//            it == songId
//        }?.let { true } ?: false

    fun removeFromFavourite(songId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
//            dao.deleteFromFavourite(
//                listOfId = dao.getAllIdOnSongId(songId)
//            )
        }
    }

    fun insertIntoAlbum(list: List<ResponseAlbum>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.forEach { album ->
                dao.insertIntoAlbum(
                    data = AlbumTable(
                        albumId = album.id,
                        name = album.name
                    )
                )?.let { albumId ->
                    if (albumId != -1L)
                        album.listOfSongs.forEach { song ->
                            dao.insetIntoAlbumSongTable(
                                data = song.toAlbumTableEntry()
                            )?.let { songId ->
                                if (songId != -1L)
                                    dao.insertIntoSongAlbumRelationTable(
                                        data = SongAlbumRelationTable(
                                            songId = songId,
                                            albumId = albumId
                                        )
                                    )
                            }
                        }
                }
            }
        }
    }


    fun insertIntoRecentlyPlayedPrev(list: List<SongPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertIntoRecentlyPlayedPrevTable(
                entrys = list.toHistoryPrevSongEntry()
            )
        }
    }

    fun readAllAlbumPreview() = dao.readAllAlbumPreview()
    fun readAllArtist() = dao.readAllArtist()

    suspend fun checkIfPlaylistIdPinned(name: String) =
        dao.checkIfPlaylistIsPinned(name)?.let { true } ?: false

    suspend fun checkIfAlbumPinned(name: String) =
        dao.checkIfAlbumIsPinned(name)?.let { true } ?: false

    suspend fun checkIfArtistPinned(name: String) =
        dao.checkIfArtistPinned(name)?.let { true } ?: false

    suspend fun addToPinnedTable(
        type: PinnedDataType,
        name: String,
        ds: DataStoreOperation
    ) = withContext(Dispatchers.IO) {
        when (type) {
            PinnedDataType.PLAYLIST -> {
                val pair = async {
                    dao.getIdOfPlaylist(name)
                }.await()

                dao.addToPinnedTable(
                    data = PinnedTable(
                        playlistId = pair.id
                    )
                )

                pair.originalId
            }

            PinnedDataType.ARTIST -> {
                val id = async {
                    dao.getIdOfArtist(name)
                }.await()

                dao.addToPinnedTable(
                    data = PinnedTable(
                        artistId = id
                    )
                )

                id
            }

            PinnedDataType.ALBUM -> {
                val pair = async {
                    dao.getIdOfAlbum(name)
                }.await()

                dao.addToPinnedTable(
                    data = PinnedTable(
                        albumId = pair.id
                    )
                )

                pair.originalId
            }

            PinnedDataType.FAVOURITE -> {
                ds.storeFavouritePinnedState(true)

                -1L
            }

            else -> -1L
        }
    }

    suspend fun removeFromPinnedTable(
        type: PinnedDataType,
        name: String,
        ds: DataStoreOperation
    ) = withContext(Dispatchers.IO) {
        when (type) {
            PinnedDataType.PLAYLIST -> {
                val pair = dao.getIdOfPlaylist(name)

                return@withContext try {
                    dao.removePlaylistIdFromPinnedTable(pair.id).let { pair.originalId }
                } catch (e: Exception) {
                    -1L
                }
            }

            PinnedDataType.ARTIST -> {
                val artistId = dao.getIdOfArtist(name)

                return@withContext try {
                    dao.removeArtistIdFromPinnedTable(artistId).let { artistId }
                } catch (e: Exception) {
                    -1L
                }
            }

            PinnedDataType.ALBUM -> {
                val pair = dao.getIdOfAlbum(name)

                return@withContext try {
                    dao.removeAlbumIdFromPinnedTable(pair.id).let { pair.originalId }
                } catch (e: Exception) {
                    -1L
                }
            }

            PinnedDataType.FAVOURITE -> {
                ds.storeFavouritePinnedState(false)
                -1L
            }

            else -> -1L
        }
    }


    suspend fun deletePlaylistArtistAlbumFavouriteEntry(
        type: PinnedDataType,
        name: String,
        ds: DataStoreOperation
    ) = withContext(Dispatchers.IO) {
        when (type) {
            PinnedDataType.PLAYLIST -> {
                val pair = dao.getIdOfPlaylist(name)

                return@withContext try {
                    dao.deletePlaylist(pair.id)
                    pair.originalId
                } catch (e: Exception) {
                    -1L
                }
            }

            PinnedDataType.ALBUM -> {
                val pair = dao.getIdOfAlbum(name)

                return@withContext try {
                    dao.deleteAlbum(pair.id)
                    pair.originalId
                } catch (e: Exception) {
                    -1L
                }
            }

            PinnedDataType.ARTIST -> {
                val artistId = dao.getIdOfArtist(name)

                return@withContext try {
                    dao.deleteArtist(artistId)
                    artistId
                } catch (e: Exception) {
                    -1L
                }
            }

            PinnedDataType.FAVOURITE -> {
                dao.deleteFavourites()
                ds.storeFavouritePinnedState(false)
                -1L
            }

            else -> -1L
        }
    }

    fun readPinnedPlaylist() = dao.readPinnedPlaylist()
    fun readPinnedAlbum() = dao.readPinnedAlbum()
    fun readPinnedArtist() = dao.readPinnedArtist()


    // songView screen query
    fun getPlaylistName(id: Long) = dao.getPlaylistName(id)
    fun getPlaylist(id: Long) = dao.getPlaylist(id)


    suspend fun getAlbum(id: Long) = withContext(Dispatchers.IO) {
        dao.getAlbum(id).let { songs ->
            UiAlbum(
                name = async { dao.getAlbumName(id) }.await(),
                totalTime = async {
                    songs.map { single ->
                        (single.totalTime.toFloatOrNull() ?: 0F) / 1000 / 60
                    }.sum().toInt().toString()
                }.await(),
                listOfSong = songs
            )
        }
    }

    fun getAllFavouriteSongs() = dao.getAllFavouriteSongs()

    suspend fun getArtistCoverImage(id: Long) = dao.getArtistCoverImage(id)

    suspend fun checkIfDailyMixTableEmpty() = dao.checkIfDailyMixTableEmpty().toInt() == 0

    fun insertIntoDailyMix(entrys: List<ResponseSong>) = CoroutineScope(Dispatchers.IO).launch {
        entrys.toDailyMixEntry().let {
            dao.insertIntoDailyMix(it)
        }
    }

    fun readAllDailyMix() = dao.readAllDailyMix()

    suspend fun checkIfArtistMixIsEmpty() = dao.checkIfArtistMixIsEmpty().toInt() == 0

    fun insertIntoArtistMix(entrys: List<ResponseSong>) = CoroutineScope(Dispatchers.IO).launch {
        entrys.toArtistMixEntry().let {
            dao.insertIntoArtistMix(it)
        }
    }

    fun readAllArtistMix() = dao.readAllArtistMix()


    suspend fun checkIfAlbumAlreadyInLibrary(albumId: Long) =
        dao.checkIfAlbumAlreadyInLibrary(albumId)?.let { true } ?: false

    suspend fun isDailyMixEmpty() = dao.isDailyMixEmpty().isEmpty()

    suspend fun getSongIdListOfDailyMix() = dao.getSongIdListOfDailyMix()

    suspend fun isPlaylistNameDuplicate(name: String) =
        dao.playlistNameDuplicityCheck(name).isNotEmpty()

    fun insertIntoPlaylist(response: ResponsePlaylist) {
        insertIntoPlaylist(
            data = response.listOfSongs,
            id = response.id,
            playlistName = response.name
        )
    }

    suspend fun isArtistMixEmpty() = dao.isArtistMixEmpty().isEmpty()

    suspend fun getSongIdListOfArtistMix() = dao.getSongIdListOfArtistMix()

    suspend fun searchPlaylist(query: String) = dao.searchPlaylist(query)

    fun addToPlaylist(entry: PlaylistSongTable, playlistId: List<Long>) {
        CoroutineScope(Dispatchers.IO).launch {
            async { dao.insertIntoPlaylistSongTable(listOf(entry)) }.await()

            dao.insertIntoSongPlaylistRelation(
                entrys = playlistId.map {
                    SongPlaylistRelationTable(
                        playlistId = it,
                        songId = entry.songId
                    )
                }
            )
        }
    }

// ---------------------------------------------------------------------------------------------

    // remove tables
    fun removeAllTable() = CoroutineScope(Dispatchers.IO).launch {
        val al = async { dao.dropAlbumTable() }
        val arPrevSong = async { dao.dropArtistPrevSongTable() }
        val daiMix = async { dao.dropDailyMixTable() }
        val arMix = async { dao.dropArtistMixTable() }
        val favArMixPrev = async { dao.dropFavArtistMixPrevTable() }
        val pin = async { dao.dropPinnedTable() }
        val playlist = async { dao.dropPlaylistTable() }
        val revPlaPrev = async { dao.dropRecentlyPlayedPrevTable() }
        val songAl = async { dao.dropSongAlbumTable() }
        val soPlay = async { dao.dropSongPlaylistTable() }

        al.await()
        arPrevSong.await()
        daiMix.await()
        arMix.await()
        favArMixPrev.await()
        pin.await()
        playlist.await()
        revPlaPrev.await()
        songAl.await()
        soPlay.await()
    }


// ---------------------------------------------------------------------------------------------

    // internal database
    fun addToInternalPinnedTable(data: InternalPinnedTable) {
        CoroutineScope(Dispatchers.IO).launch {
            intDao.addToPinnedTable(data)
        }
    }

    fun removeFromPinnedTable(data: InternalPinnedTable, response: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            intDao.checkIfPinnedIdPresent(
                pinnedId = data.pinnedId,
                type = data.type
            )?.let {
                intDao.removeFromPinnedTable(
                    pinnedId = data.pinnedId,
                    type = data.type,
                    operation = PinnedOperation.ADD
                )

                return@launch
            }

            if (!response) intDao.addToPinnedTable(data)
        }
    }

    fun addToInternalItemTable(data: InternalItemTable) {
        CoroutineScope(Dispatchers.IO).launch {
            intDao.addTopItemTable(data)
        }
    }
}
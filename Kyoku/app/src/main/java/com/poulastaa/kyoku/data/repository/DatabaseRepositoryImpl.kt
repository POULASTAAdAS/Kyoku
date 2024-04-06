package com.poulastaa.kyoku.data.repository

import android.content.Context
import com.poulastaa.kyoku.data.database.AppDao
import com.poulastaa.kyoku.data.database.InternalDao
import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import com.poulastaa.kyoku.data.model.api.service.home.AlbumPreview
import com.poulastaa.kyoku.data.model.api.service.home.DailyMixPreview
import com.poulastaa.kyoku.data.model.api.service.home.FevArtistsMixPreview
import com.poulastaa.kyoku.data.model.api.service.home.ResponseAlbum
import com.poulastaa.kyoku.data.model.api.service.home.ResponseArtistsPreview
import com.poulastaa.kyoku.data.model.api.service.home.ResponsePlaylist
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import com.poulastaa.kyoku.data.model.api.service.pinned.PinnedOperation
import com.poulastaa.kyoku.data.model.database.PlaylistWithSongs
import com.poulastaa.kyoku.data.model.database.table.AlbumPreviewSongRelationTable
import com.poulastaa.kyoku.data.model.database.table.AlbumTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPreviewSongRelation
import com.poulastaa.kyoku.data.model.database.table.FavouriteTable
import com.poulastaa.kyoku.data.model.database.table.PinnedTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.RecentlyPlayedPrevTable
import com.poulastaa.kyoku.data.model.database.table.SongAlbumRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalItemTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalPinnedTable
import com.poulastaa.kyoku.data.model.screens.library.PinnedDataType
import com.poulastaa.kyoku.data.model.screens.song_view.UiAlbum
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.toAlbumTablePrevEntry
import com.poulastaa.kyoku.utils.toArtistMixEntry
import com.poulastaa.kyoku.utils.toArtistTableEntry
import com.poulastaa.kyoku.utils.toDailyMixEntry
import com.poulastaa.kyoku.utils.toDailyMixPrevEntry
import com.poulastaa.kyoku.utils.toFevArtistMixPrevTable
import com.poulastaa.kyoku.utils.toSongPrevTableEntry
import com.poulastaa.kyoku.utils.toSongTable
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
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

    fun insertIntoPlaylistSpotify(
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

            data.forEach {
                dao.insertSongPlaylistRelation(
                    data = SongPlaylistRelationTable(
                        playlistId = playlistId,
                        songId = async {
                            dao.insertSong(
                                song = it.toSongTable()
                            )
                        }.await()
                    )
                )
            }
        }
    }

    fun getAllPlaylist(): Flow<List<PlaylistWithSongs>> = dao.getAllPlaylist()

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
                list.forEach {
                    dao.insertIntoFevArtistMixPrev(
                        data = it.toFevArtistMixPrevTable()
                    )
                }
            }.await()
        }
    }

    fun insertIntoAlbumPrev(list: List<AlbumPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            async {
                list.forEach {
                    val albumId = dao.insertIntoAlbumPrev(data = it.toAlbumTablePrevEntry())

                    it.listOfSongs.forEach { song ->
                        val songId = dao.insertIntoSongPrev(
                            data = song.toSongPrevTableEntry()
                        )

                        dao.insertIntoAlbumPrevSongRelationTable(
                            data = AlbumPreviewSongRelationTable(
                                albumId = albumId,
                                songId = songId
                            )
                        )
                    }
                }
            }.await()
        }
    }

    fun insertResponseArtistPrev(list: List<ResponseArtistsPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            async {
                list.forEach {
                    try {
                        dao.insertIntoArtist(
                            it.artist.toArtistTableEntry()
                        )
                    } catch (e: Exception) {
                        null
                    }?.let { id ->
                        it.listOfSongs.forEach { previewSong ->
                            val songId = dao.insertIntoSongPrev(
                                previewSong.toSongPrevTableEntry()
                            )

                            dao.insertIntoArtistPrevSongRelationTable(
                                data = ArtistPreviewSongRelation(
                                    artistId = id,
                                    songId = songId
                                )
                            )
                        }
                    }
                }
            }.await()
        }
    }

    fun insertDailyMixPrev(data: DailyMixPreview) {
        CoroutineScope(Dispatchers.IO).launch {
            val idList = dao.insertBatchIntoSongPrev(data = data.listOfSongs.toSongPrevTableEntry())

            dao.insertIntoDailyMixPrevTable(
                data = idList.toDailyMixPrevEntry()
            )
        }
    }

    fun readFevArtistMixPrev() = dao.readFevArtistPrev()
    fun readAllAlbumPrev() = dao.readAllAlbumPrev()
    fun readAllArtistPrev() = dao.readAllArtistPrev()
    suspend fun readDailyMixPrevUrls() = dao.readDailyMixPrevUrls()
    fun readPlaylistPreview() = dao.readPreviewPlaylist()
    fun redRecentlyPlayed() = dao.redRecentlyPlayed()
    fun radSavedAlbumPrev() = dao.radSavedAlbumPrev()
    suspend fun readFavouritePrev() = dao.readFavouritePrev()


    fun insertIntoPlaylistHome(list: List<ResponsePlaylist>) {
        CoroutineScope(Dispatchers.IO).launch {
            async {
                list.forEach {
                    val playlistId = async {
                        dao.insertPlaylist(
                            playlist = PlaylistTable(
                                playlistId = it.id,
                                name = it.name
                            )
                        )
                    }.await()

                    it.listOfSongs.forEach { song ->
                        val songId = async {
                            dao.insertSong(
                                song = song.toSongTable()
                            )
                        }.await()

                        dao.insertSongPlaylistRelation(
                            data = SongPlaylistRelationTable(
                                playlistId = playlistId,
                                songId = songId
                            )
                        )
                    }
                }
            }.await()
        }
    }

    fun insertIntoFavourite(list: List<ResponseSong>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.forEach {
                dao.insertIntoFavourite(
                    data = FavouriteTable(
                        songId = dao.insertSong(
                            song = it.toSongTable()
                        )
                    )
                )
            }
        }
    }

    fun insertIntoAlbum(list: List<ResponseAlbum>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.forEach {
                val albumId = async {
                    dao.insertIntoAlbum(
                        data = AlbumTable(
                            albumId = it.id,
                            name = it.name
                        )
                    )
                }.await()

                it.listOfSongs.forEach { song ->
                    val songId = async {
                        dao.insertSong(
                            song = song.toSongTable()
                        )
                    }.await()

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


    fun insertIntoRecentlyPlayedPrev(list: List<SongPreview>) {
        CoroutineScope(Dispatchers.IO).launch {
            async {
                list.forEach {
                    val songId = async {
                        dao.insertIntoSongPrev(
                            data = it.toSongPrevTableEntry()
                        )
                    }.await()


                    dao.insertIntoRecentlyPlayedPrevTable(
                        data = RecentlyPlayedPrevTable(
                            songId = songId
                        )
                    )
                }
            }.await()
        }
    }

    fun readAllAlbum() = dao.readAllAlbum()
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
    fun getPlaylist(id: Long) = dao.getPlaylist(id)
    suspend fun getAlbum(name: String) = dao.getAlbum(name).groupBy {
        it.album
    }.map {
        UiAlbum(
            name = it.key,
            listOfSong = it.value
        )
    }.firstOrNull() ?: UiAlbum()

    suspend fun getAllFavouriteSongs() = dao.getAllFavouriteSongs()

    suspend fun getArtistCoverImage(id: Long) = dao.getArtistCoverImage(id)

    suspend fun checkIfDailyMixTableEmpty() = dao.checkIfDailyMixTableEmpty().toInt() == 0

    fun insertIntoDailyMix(entrys: List<SongPreview>) = CoroutineScope(Dispatchers.IO).launch {
        entrys.toDailyMixEntry().let {
            dao.insertIntoDailyMix(it)
        }
    }

    fun readAllDailyMix() = dao.readAllDailyMix()


    suspend fun checkIfArtistMixIsEmpty() = dao.checkIfArtistMixIsEmpty().toInt() == 0

    fun insertIntoArtistMix(entrys: List<SongPreview>) = CoroutineScope(Dispatchers.IO).launch {
        entrys.toArtistMixEntry().let {
            dao.insertIntoArtistMix(it)
        }
    }

    fun readAllArtistMix() = dao.readAllArtistMix()


    suspend fun checkIfSongAlreadyInFavourite(songId: Long) =
        dao.checkIfSongAlreadyInFavourite(songId)?.let { true } ?: false

    suspend fun checkIfAlbumAlreadyInLibrary(albumId: Long) =
        dao.checkIfAlbumAlreadyInLibrary(albumId)?.let { true } ?: false

    // remove tables
    fun removeAllTable() = CoroutineScope(Dispatchers.IO).launch {
        val alPrev = async { dao.dropAlbumPrevTable() }
        val alPrevSong = async { dao.dropAlbumPrevSongTable() }
        val al = async { dao.dropAlbumTable() }
        val arPrev = async { dao.dropArtistPrevTable() }
        val arPrevSong = async { dao.dropArtistPrevSongTable() }
        val daiMixPrev = async { dao.dropDailyMixPrevTable() }
        val daiMix = async { dao.dropDailyMixTable() }
        val arMix = async { dao.dropArtistMixTable() }
        val fav = async { dao.dropFavouriteTable() }
        val favArMixPrev = async { dao.dropFavArtistMixPrevTable() }
        val pin = async { dao.dropPinnedTable() }
        val playlist = async { dao.dropPlaylistTable() }
        val revPlaPrev = async { dao.dropRecentlyPlayedPrevTable() }
        val songAl = async { dao.dropSongAlbumTable() }
        val soPlay = async { dao.dropSongPlaylistTable() }
        val soPrev = async { dao.dropSongPrevTable() }
        val song = async { dao.dropSongTable() }

        alPrev.await()
        alPrevSong.await()
        al.await()
        arPrev.await()
        arPrevSong.await()
        daiMixPrev.await()
        daiMix.await()
        arMix.await()
        fav.await()
        favArMixPrev.await()
        pin.await()
        playlist.await()
        revPlaPrev.await()
        songAl.await()
        soPlay.await()
        soPrev.await()
        song.await()
    }


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
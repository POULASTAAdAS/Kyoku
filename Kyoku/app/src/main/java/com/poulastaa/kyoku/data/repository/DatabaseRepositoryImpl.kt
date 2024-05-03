package com.poulastaa.kyoku.data.repository

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import coil.ImageLoader
import coil.request.ImageRequest
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
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.SongAlbumRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalItemTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalPinnedTable
import com.poulastaa.kyoku.data.model.database.table.prev.PlayingQueueTable
import com.poulastaa.kyoku.data.model.screens.home.SongType
import com.poulastaa.kyoku.data.model.screens.library.Artist
import com.poulastaa.kyoku.data.model.screens.library.PinnedDataType
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiModel
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.BitmapConverter
import com.poulastaa.kyoku.utils.ColorType
import com.poulastaa.kyoku.utils.PaletteGenerator
import com.poulastaa.kyoku.utils.toAlbumTableEntry
import com.poulastaa.kyoku.utils.toAlbumTablePrevEntry
import com.poulastaa.kyoku.utils.toArtistMixEntry
import com.poulastaa.kyoku.utils.toArtistSongEntry
import com.poulastaa.kyoku.utils.toArtistTableEntry
import com.poulastaa.kyoku.utils.toDailyMixEntry
import com.poulastaa.kyoku.utils.toFavouriteTableEntry
import com.poulastaa.kyoku.utils.toFavouriteTableEntryList
import com.poulastaa.kyoku.utils.toHistoryPrevSongEntry
import com.poulastaa.kyoku.utils.toPlayingQueueTable
import com.poulastaa.kyoku.utils.toPlaylistSongTable
import com.poulastaa.kyoku.utils.toReqAlbumEntry
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    suspend fun insertIntoPlaylist(
        data: List<ResponseSong>,
        id: Long,
        playlistName: String
    ) {
        withContext(Dispatchers.IO) {
            val playlistId = async {
                dao.insertPlaylist(
                    playlist = PlaylistTable(
                        playlistId = id,
                        name = playlistName
                    )
                )
            }.await()

            async {
                data.map {
                    it.toPlaylistSongTable()
                }.map {
                    async {
                        it to loadDrawable(it.coverImage)
                    }
                }.awaitAll().map {
                    try {
                        it.first to encodeCover(it.second)
                    } catch (_: Exception) {
                        it.first to it.first.coverImage
                    }
                }.map {
                    it.first.copy(
                        coverImage = it.second
                    )
                }.let {
                    dao.insertIntoPlaylistSongTable(entrys = it)
                }
            }.await()

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

    suspend fun insertIntoFevArtistMixPrev(list: List<FevArtistsMixPreview>) {
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

    suspend fun insertIntoAlbumPrev(list: List<AlbumPreview>) {
        withContext(Dispatchers.IO) {
            list.toAlbumTablePrevEntry().map {
                async {
                    it to loadDrawable(it.coverImage)
                }
            }.awaitAll().map {
                try {
                    it.first to encodeCover(it.second)
                } catch (_: Exception) {
                    it.first to it.first.coverImage
                }
            }.map {
                it.first.copy(
                    coverImage = it.second
                )
            }.let {
                dao.insertIntoPrevAlbum(it)
            }
        }
    }

    suspend fun insertResponseArtistPrev(list: List<ResponseArtistsPreview>) {
        withContext(Dispatchers.IO) {
            async {
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
            }.await()

            if (header != null && context != null) {
                val artist = async {
                    dao.getAllArtistIdAndCover().map {
                        async {
                            it.id to loadDrawable(it.cover)
                        }
                    }.awaitAll().mapNotNull {
                        try {
                            it.first to encodeCover(it.second)
                        } catch (_: Exception) {
                            null
                        }
                    }.forEach {
                        dao.updateArtistCover(it.first, it.second)
                    }
                }

                val song = async {
                    dao.getAllArtistSongIdAndCover().map {
                        async {
                            it.id to loadDrawable(it.cover)
                        }
                    }.awaitAll().mapNotNull {
                        try {
                            it.first to encodeCover(it.second)
                        } catch (_: Exception) {
                            null
                        }
                    }.forEach {
                        dao.updateArtistSongCover(it.first, it.second)
                    }
                }

                artist.await()
                song.await()
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


    suspend fun insertIntoPlaylist(list: List<ResponsePlaylist>) {
        withContext(Dispatchers.IO) {
            list.map {
                async {
                    insertIntoPlaylist(
                        data = it.listOfSongs,
                        id = it.id,
                        playlistName = it.name
                    )
                }
            }.awaitAll()
        }
    }

    suspend fun insertIntoFavourite(list: List<ResponseSong>) {
        withContext(Dispatchers.IO) {
            list.toFavouriteTableEntryList().map {
                async {
                    it to loadDrawable(it.coverImage)
                }
            }.awaitAll().map {
                try {
                    it.first to encodeCover(it.second)
                } catch (_: Exception) {
                    it.first to it.first.coverImage
                }
            }.map {
                it.first.copy(
                    coverImage = it.second
                )
            }.let {
                dao.insertIntoFavourite(it)
            }
        }
    }

    suspend fun checkIfSongAlreadyInFavourite(songId: Long) =
        dao.getFavouriteSong(songId)?.let { true } ?: false

    fun removeFromFavourite(songId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.removeFromFavourite(songId = songId)
        }
    }

    suspend fun insertIntoAlbum(list: List<ResponseAlbum>) {
        withContext(Dispatchers.IO) {
            async {
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
            }.await()

            if (context != null && header != null) {
                async {
                    dao.getAllAlbumIdAndCover().map {
                        async {
                            it.id to loadDrawable(it.cover)
                        }
                    }.awaitAll().mapNotNull {
                        try {
                            it.first to encodeCover(it.second)
                        } catch (_: Exception) {
                            null
                        }
                    }.forEach {
                        dao.updateAlbumSongCOver(it.first, it.second)
                    }
                }.await()
            }
        }
    }


    suspend fun insertIntoRecentlyPlayedPrev(list: List<SongPreview>) {
        withContext(Dispatchers.IO) {
            async {
                list.toHistoryPrevSongEntry().map {
                    async { it to loadDrawable(it.coverImage) }
                }.awaitAll().map {
                    try {
                        it.first to encodeCover(it.second)
                    } catch (_: Exception) {
                        it.first to it.first.coverImage
                    }
                }.map {
                    it.first.copy(
                        coverImage = it.second
                    )
                }.let {
                    dao.insertIntoRecentlyPlayedPrevTable(
                        entrys = it
                    )
                }
            }.await()
        }
    }

    fun readAllAlbumPreview() = dao.readAllAlbumPreview()
    fun readAllArtist() = dao.readAllArtist()

    suspend fun checkIfPlaylistIdPinned(name: String) =
        dao.checkIfPlaylistIsPinned(name)?.let { true } ?: false

    suspend fun checkIfAlbumPinned(name: String) =
        dao.checkIfAlbumIsPinned(name)?.let { true } ?: false

    suspend fun checkIfArtistPinned(id: Long) =
        dao.checkIfArtistPinned(id)?.let { true } ?: false

    suspend fun addToPinnedTable(
        type: PinnedDataType,
        name: String,
        ds: DataStoreOperation
    ) = withContext(Dispatchers.IO) {
        when (type) {
            PinnedDataType.PLAYLIST -> {
                val pair = async {
                    dao.getPlaylistIds(name)
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
                val pair = dao.getPlaylistIds(name)

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


    suspend fun removePlaylistArtistAlbumFavouriteEntry(
        type: PinnedDataType,
        name: String,
        ds: DataStoreOperation
    ) = withContext(Dispatchers.IO) {
        when (type) {
            PinnedDataType.PLAYLIST -> {
                val pair = dao.getPlaylistIds(name)

                return@withContext try {
                    dao.removePlaylist(pair.id)
                    pair.originalId
                } catch (e: Exception) {
                    -1L
                }
            }

            PinnedDataType.ALBUM -> {
                val pair = dao.getIdOfAlbum(name)

                return@withContext try {
                    dao.removeAlbum(pair.id)
                    pair.originalId
                } catch (e: Exception) {
                    -1L
                }
            }

            PinnedDataType.ARTIST -> {
                val artistId = dao.getIdOfArtist(name)

                return@withContext try {
                    dao.removeArtist(artistId)
                    artistId
                } catch (e: Exception) {
                    -1L
                }
            }

            PinnedDataType.FAVOURITE -> {
                dao.removeFavourites()
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
    suspend fun getPlaylistId(name: String) = dao.getPlaylistId(name)


    suspend fun getAlbum(id: Long) = withContext(Dispatchers.IO) {
        dao.getAlbum(id).let { songs ->
            if (songs.isEmpty()) return@let SongViewUiModel()

            SongViewUiModel(
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

    suspend fun getAlbumId(name: String) = dao.getAlbumId(name)

    suspend fun getReqAlbum(albumId: Long) = dao.getReqAlbum(albumId)
    suspend fun getPrevAlbumId(name: String) = dao.getPrevAlbumId(name)

    fun getAllFavouriteSongs() = dao.getAllFavouriteSongs()

    suspend fun getArtistCoverImage(id: Long) = dao.getArtistCoverImage(id)
    suspend fun getArtistCoverImage(name: String) = dao.getArtistCoverImage(name)
    suspend fun getArtistId(name: String) = dao.getIdOfArtist(name)
    suspend fun getArtistOnId(id: Long) = dao.getArtistOnId(id)?.let {
        Artist(
            artistId = it.artistId,
            name = it.name,
            coverImage = it.coverImage
        )
    }

    suspend fun checkIfDailyMixTableEmpty() = dao.checkIfDailyMixTableEmpty().toInt() == 0

    suspend fun insertIntoDailyMix(entrys: List<ResponseSong>) =
        withContext(Dispatchers.IO) {
            async {
                entrys.toDailyMixEntry().let {
                    dao.insertIntoDailyMix(it)
                }
            }.await()


            if (header != null && context != null) {
                val listPair = dao.getDailyMixImageUrlAndId().map {
                    async { it.id to loadDrawable(it.cover) }
                }

                listPair.awaitAll().mapNotNull {
                    try {
                        it.first to encodeCover(it.second)
                    } catch (_: Exception) {
                        null
                    }
                }.forEach {
                    dao.updateDailyMixUrl(it.first, it.second)
                }
            }
        }

    fun readAllDailyMix() = dao.readAllDailyMix()

    suspend fun checkIfArtistMixIsEmpty() = dao.checkIfArtistMixIsEmpty().toInt() == 0

    suspend fun insertIntoArtistMix(entrys: List<ResponseSong>) = withContext(Dispatchers.IO) {
        async {
            entrys.toArtistMixEntry().let {
                dao.insertIntoArtistMix(it)
            }
        }.await()

        if (header != null && context != null) {
            val listPair = dao.getArtistMixImageUrlAndId().map {
                async { it.id to loadDrawable(it.cover) }
            }

            listPair.awaitAll().mapNotNull {
                try {
                    it.first to encodeCover(it.second)
                } catch (_: Exception) {
                    null
                }
            }.forEach {
                dao.updateArtistMixUrl(it.first, it.second)
            }
        }
    }

    fun readAllArtistMix() = dao.readAllArtistMix()


    suspend fun checkIfAlbumAlreadyInLibrary(albumId: Long) =
        dao.checkIfAlbumAlreadyInLibrary(albumId)?.let { true } ?: false

    suspend fun isDailyMixEmpty() = dao.isDailyMixEmpty().isEmpty()

    suspend fun getSongIdListOfDailyMix() = dao.getSongIdListOfDailyMix()

    suspend fun isPlaylistNameDuplicate(name: String) =
        dao.playlistNameDuplicityCheck(name).isNotEmpty()

    suspend fun insertIntoPlaylist(response: ResponsePlaylist) {
        insertIntoPlaylist(
            data = response.listOfSongs,
            id = response.id,
            playlistName = response.name
        )
    }

    suspend fun isArtistMixEmpty() = dao.isArtistMixEmpty().isEmpty()

    suspend fun getSongIdListOfArtistMix() = dao.getSongIdListOfArtistMix()

    suspend fun searchPlaylist(query: String) = dao.searchPlaylist(query)

    suspend fun getPlaylistIdOnSongId(songId: Long) = dao.getPlaylistIdOnSongId(songId)

    fun editPlaylist(
        song: ResponseSong,
        isFavourite: Boolean,
        addList: List<Long>,
        removeList: List<Long>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isFavourite) dao.insertOneIntoFavourite(song.toFavouriteTableEntry())
            else removeFromFavourite(song.id)
        }

        CoroutineScope(Dispatchers.IO).launch {
            if (removeList.isNotEmpty())
                async {
                    val playlistIdList = dao.getPlaylistId(removeList)

                    playlistIdList.forEach {
                        dao.removeFromPlaylist(
                            songId = song.id,
                            playlistId = it
                        )
                    }
                }.await()

            if (addList.isNotEmpty()) {
                val playlistIdList = dao.getPlaylistId(addList)

                async {
                    dao.insertIntoPlaylistSongTable(
                        entry = song.toPlaylistSongTable()
                    )
                }.await()

                dao.insertIntoSongPlaylistRelation(
                    entrys = playlistIdList.map {
                        SongPlaylistRelationTable(
                            playlistId = it,
                            songId = song.id
                        )
                    }
                )
            }
        }
    }

    fun removeAlbum(id: Long) = CoroutineScope(Dispatchers.IO).launch {
        dao.removeAlbum(id)
    }

    suspend fun getAlbumOnAlbumId(albumId: Long) = dao.getAlbumOnAlbumId(albumId)

    suspend fun insertIntoReqAlbum(entry: ResponseAlbum) {
        withContext(Dispatchers.IO) {
            val data = entry.listOfSongs.toReqAlbumEntry(
                albumId = entry.id,
                albumName = entry.name
            ).map {
                async {
                    it to loadDrawable(it.coverImage)
                }
            }.awaitAll().map {
                try {
                    it.first to encodeCover(it.second)
                } catch (e: Exception) {
                    it.first to it.first.coverImage
                }
            }.map {
                it.first.copy(
                    coverImage = it.second
                )
            }

            dao.insertIntoReqAlbum(
                entrys = data
            )
        }
    }

    fun readFromReqAlbum() = dao.readFromReqAlbum()

    fun removeAllFromReqAlbum() {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllFromReqAlbum()
        }
    }

    fun removeFromRecentlyPlayed(songId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.removeFromRecentlyPlayed(songId)
        }
    }

    suspend fun checkIfAlreadyInPlayingQueue(songId: Long) =
        dao.checkIfAlreadyInPlayingQueue(songId)


    suspend fun insertIntoPlayingQueueTable(song: ResponseSong, songType: SongType) {
        val songId = checkIfAlreadyInPlayingQueue(song.id)

        if (songId != null) return

        when (songType) {
            SongType.HISTORY_SONG -> {
                withContext(Dispatchers.IO) {
                    val clear = async { dao.clearPlayingQueue() }
                    val coverDef = async { dao.getHistorySongCoverOnSongId(song.id) }

                    clear.await()
                    val cover = coverDef.await()

                    dao.insertIntoPlayingQueueTable(
                        entry = if (cover != null) song.toPlayingQueueTable().copy(
                            coverImage = cover
                        )
                        else song.toPlayingQueueTable().let {
                            async {
                                it to loadDrawable(it.coverImage)
                            }.await()
                        }.let {
                            try {
                                it.first to encodeCover(it.second)
                            } catch (_: Exception) {
                                it.first to it.first.coverImage
                            }
                        }.let {
                            val bitmap = BitmapConverter.decodeToBitmap(it.second)

                            val vibrant =
                                if (bitmap != null) PaletteGenerator.extractColorFromBitMap(bitmap)[ColorType.LIGHT_MUTED]
                                    ?: "#FFB3CEA6"
                                else "#FFB3CEA6"

                            val darkVibrant =
                                if (bitmap != null) PaletteGenerator.extractColorFromBitMap(bitmap)[ColorType.DARK_MUTED]
                                    ?: "#FF354D2E"
                                else "#FF354D2E"

                            it.first.copy(
                                coverImage = it.second,
                                colorOne = vibrant,
                                colorTwo = darkVibrant
                            )
                        }
                    )
                }
            }

            SongType.ARTIST_SONG -> {
                withContext(Dispatchers.IO) {
                    val clear = async { dao.clearPlayingQueue() }
                    val coverDef = async { dao.getArtistSongCoverOnSongId(song.id) }

                    clear.await()
                    val cover = coverDef.await()

                    dao.insertIntoPlayingQueueTable(
                        entry = if (cover != null) song.toPlayingQueueTable().copy(
                            coverImage = cover
                        )
                        else song.toPlayingQueueTable().let {
                            async {
                                it to loadDrawable(it.coverImage)
                            }.await()
                        }.let {
                            try {
                                it.first to encodeCover(it.second)
                            } catch (_: Exception) {
                                it.first to it.first.coverImage
                            }
                        }.let {
                            val bitmap = BitmapConverter.decodeToBitmap(it.second)

                            val vibrant =
                                if (bitmap != null) PaletteGenerator.extractColorFromBitMap(bitmap)[ColorType.LIGHT_MUTED]
                                    ?: "#FFB3CEA6"
                                else "#FFB3CEA6"

                            val darkVibrant =
                                if (bitmap != null) PaletteGenerator.extractColorFromBitMap(bitmap)[ColorType.DARK_MUTED]
                                    ?: "#FF354D2E"
                                else "#FF354D2E"

                            it.first.copy(
                                coverImage = it.second,
                                colorOne = vibrant,
                                colorTwo = darkVibrant
                            )
                        }
                    )
                }
            }

            else -> return
        }
    }

    fun insertIntoPlayingQueueTable(entrys: List<PlayingQueueTable>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertIntoPlayingQueueTable(entrys)
        }
    }

    fun readAllFromPlayingQueue() = dao.readAllFromPlayingQueue()

    suspend fun clearPlayingQueue() = dao.clearPlayingQueue()

    suspend fun hideSong(songId: Long, name: String) = withContext(Dispatchers.IO) {
        val artistIdDef = async { dao.getIdOfArtist(name) }
        val idDef = async { dao.getArtistSongId(songId) }

        dao.removeFromSongArtistRelation(artistIdDef.await(), idDef.await())

        //todo add to hidden song table
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

    private suspend fun loadDrawable(cover: String) =
        (ImageLoader(context!!).execute(
            ImageRequest.Builder(context!!)
                .addHeader(
                    if (!header!!.startsWith("B")) "Cookie" else "Authorization",
                    header!!
                )
                .data(cover)
                .build()
        )).drawable

    private fun encodeCover(drawable: Drawable?) =
        BitmapConverter.encodeToSting((drawable as BitmapDrawable).bitmap)
}


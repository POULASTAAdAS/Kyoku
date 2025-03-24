package com.poulastaa.sync.repository

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.domain.repository.sync.LocalSyncDatasource
import com.poulastaa.sync.domain.repository.SynRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class SyncRepositoryService(
    private val db: LocalSyncDatasource,
) : SynRepository {
    override suspend fun <T> syncData(
        type: DtoSyncType,
        savedIdList: List<Long>,
        payload: ReqUserPayload,
    ): DtoSyncPayload<T>? {
        val user = db.getUsersByEmail(payload.email, payload.userType) ?: return null

        return when (type) {
            DtoSyncType.SYNC_ALBUM -> {
                val dbList = db.getSavedAlbumIdList(user.id)

                val removeList = savedIdList.filterNot { it in dbList }
                val new = dbList.filterNot { it in savedIdList }
                if (removeList.isNotEmpty()) db.removeAlbum(removeList, user.id)

                DtoSyncPayload(
                    removeIdList = removeList,
                    newData = db.getFullAlbumOnIdList(new) as List<T>
                )
            }

            DtoSyncType.SYNC_PLAYLIST -> {
                val dbList = db.getSavedPlaylistIdList(user.id)

                val removeList = savedIdList.filterNot { it in dbList }
                val new = dbList.filterNot { it in savedIdList }
                if (removeList.isNotEmpty()) db.removePlaylist(removeList, user.id)

                DtoSyncPayload(
                    removeIdList = removeList,
                    newData = db.getFullPlaylistOnIdList(new) as List<T>
                )
            }

            DtoSyncType.SYNC_ARTIST -> {
                val dbList = db.getSavedArtistIdList(user.id)

                val removeList = savedIdList.filterNot { it in dbList }
                val new = dbList.filterNot { it in savedIdList }
                if (removeList.isNotEmpty()) db.removeArtist(removeList, user.id)

                DtoSyncPayload(
                    removeIdList = removeList,
                    newData = db.getArtistOnIdList(new) as List<T>
                )
            }

            DtoSyncType.SYNC_FAVOURITE -> {
                val dbList = db.getSavedFavouriteSongsIdList(user.id)

                val removeList = savedIdList.filterNot { it in dbList }
                val new = dbList.filterNot { it in savedIdList }
                if (removeList.isNotEmpty()) db.removeFavouriteSongs(removeList, user.id)

                DtoSyncPayload(
                    removeIdList = removeList,
                    newData = db.getFavoriteSongs(new) as List<T>
                )
            }
        }
    }

    override suspend fun syncPlaylistSongs(
        idList: List<Pair<PlaylistId, List<SongId>>>,
        payload: ReqUserPayload,
    ): DtoSyncPlaylistSongPayload? = coroutineScope {
        db.getUsersByEmail(payload.email, payload.userType) ?: return@coroutineScope null
        val result = idList.map { (playlistId, savedIdList) ->
            async {
                val dbList = db.getPlaylistSongIdList(playlistId)

                val removeList = savedIdList.filterNot { it in dbList }
                val new = dbList.filterNot { it in savedIdList }
                if (removeList.isNotEmpty()) db.removePlaylistSongs(playlistId, removeList)

                val remove = Pair(playlistId, removeList)
                val newData = Pair(playlistId, db.getPlaylistSongs(new))

                Pair(remove, newData)
            }
        }.awaitAll()

        DtoSyncPlaylistSongPayload(
            removeIdList = result.map { it.first },
            newData = result.map { it.second }
        )
    }
}
package com.poulastaa.view.data.repository

import com.poulastaa.core.domain.model.DtoViewOtherPayload
import com.poulastaa.core.domain.model.DtoViewType
import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.domain.repository.view.LocalViewDatasource
import com.poulastaa.view.domain.model.DtoViewArtistPayload
import com.poulastaa.view.domain.repository.ViewRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal class ViewRepositoryService(
    private val db: LocalViewDatasource,
) : ViewRepository {
    override suspend fun getArtist(
        artistId: ArtistId,
        userPayload: ReqUserPayload,
    ): DtoViewArtistPayload? = coroutineScope {
        db.getUserByEmail(userPayload.email, userPayload.userType) ?: return@coroutineScope null

        val artist = async { db.getArtist(artistId) }
        val songs = async { db.getArtistMostPopularSongs(artistId) }

        DtoViewArtistPayload(
            artist = artist.await() ?: return@coroutineScope null,
            songs = songs.await(),
        )
    }

    override suspend fun getViewTypeData(
        type: DtoViewType,
        otherId: Long?,
        songIds: List<SongId>?,
        payload: ReqUserPayload,
    ): DtoViewOtherPayload<Any>? {
        val user = db.getUserByEmail(payload.email, payload.userType) ?: return null

        return when (type) {
            DtoViewType.PLAYLIST -> otherId?.let { db.getPrevFullPlaylist(otherId) }
            DtoViewType.ALBUM -> otherId?.let { db.getPrevFullAlbum(otherId) }
            DtoViewType.FAVOURITE -> db.getPrevFev(user.id)
            DtoViewType.POPULAR_SONG_MIX -> songIds?.let { db.getPopularSongMix(user.id, it) }
            DtoViewType.POPULAR_YEAR_MIX -> songIds?.let {
                db.getPopularYearMix(
                    user.id,
                    user.bDate?.year ?: return null,
                    user.countryId,
                    it
                )
            }

            DtoViewType.SAVED_ARTIST_SONG_MIX -> songIds?.let { db.getPopularArtistSongMix(user.id, it) }
            DtoViewType.DAY_TYPE_MIX -> songIds?.let { db.getPopularDayTimeMix(user.id, it) }
        } as DtoViewOtherPayload<Any>
    }
}
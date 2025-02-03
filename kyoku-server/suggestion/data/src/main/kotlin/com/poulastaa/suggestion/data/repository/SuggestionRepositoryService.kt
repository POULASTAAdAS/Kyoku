package com.poulastaa.suggestion.data.repository

import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionDatasource
import com.poulastaa.suggestion.domain.model.DtoHome
import com.poulastaa.suggestion.domain.model.DtoRefresh
import com.poulastaa.suggestion.domain.repository.SuggestionRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SuggestionRepositoryService(
    private val db: LocalSuggestionDatasource,
) : SuggestionRepository {
    override suspend fun getHomeData(payload: ReqUserPayload): DtoHome? {
        val user = db.getUserByEmail(payload.email, payload.userType) ?: return null

        return coroutineScope {
            val prevPopularSongMixDef = async { db.getPrevPopularCountrySong(user.id, user.countryId) }
            val prevPopularArtistMixDef = async { db.getPrevPopularArtistMix(user.id, user.countryId) }
            val prevOldGemDef = async { db.getPrevPopularYearSongs(user.id, user.countryId) }

            val suggestedArtisteDef = async { db.getSuggestedArtist(user.id) }
            val suggestedAlbumDef = async { db.getSuggestedAlbum(user.id) }
            val suggestedArtistSongDef = async { db.getSuggestedArtistSong(user.id) }

            val playlistDef = async { db.getSavedPlaylist(user.id) }
            val albumDef = async { db.getSavedAlbum(user.id) }
            val artistDef = async { db.getSavedArtist(user.id) }

            DtoHome(
                refresh = DtoRefresh(
                    prevPopularSongMix = prevPopularSongMixDef.await(),
                    prevPopularArtistMix = prevPopularArtistMixDef.await(),
                    prevOldGem = prevOldGemDef.await(),
                    suggestedArtist = suggestedArtisteDef.await(),
                    suggestedAlbum = suggestedAlbumDef.await(),
                    suggestedArtistSong = suggestedArtistSongDef.await()
                ),
                playlist = playlistDef.await(),
                album = albumDef.await(),
                artist = artistDef.await()
            )
        }
    }
}
package com.poulastaa.suggestion.data.repository

import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionDatasource
import com.poulastaa.suggestion.data.mapper.toDtoAddSongToPlaylistPageItem
import com.poulastaa.suggestion.domain.model.*
import com.poulastaa.suggestion.domain.repository.SuggestionRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

class SuggestionRepositoryService(
    private val db: LocalSuggestionDatasource,
) : SuggestionRepository {
    override suspend fun getHomeData(payload: ReqUserPayload): DtoHome? {
        val user = db.getUserByEmail(payload.email, payload.userType) ?: return null
        if (user.bDate == null) return null

        return coroutineScope {
            val prevPopularSongMixDef = async { db.getPrevPopularCountrySong(user.id, user.countryId) }
            val prevPopularArtistMixDef = async { db.getPrevPopularArtistMix(user.id) }
            val prevOldGemDef = async { db.getPrevPopularYearSongs(user.id, user.bDate!!.year) }

            val suggestedArtisteDef = async { db.getSuggestedArtist(user.id, user.countryId) }
            val suggestedAlbumDef = async { db.getSuggestedAlbum(user.id) }

            val playlistDef = async { db.getSavedPlaylist(user.id) }
            val albumDef = async { db.getSavedAlbum(user.id) }
            val artistDef = async { db.getSavedArtist(user.id) }

            val suggestedArtist = suggestedArtisteDef.await()
            val suggestedArtistSongDef = async {
                db.getSuggestedArtistSong(
                    userId = user.id,
                    suggestedArtistIdList = suggestedArtist.map { it.id }
                )
            }

            DtoHome(
                refresh = DtoRefresh(
                    prevPopularSongMix = prevPopularSongMixDef.await(),
                    prevPopularArtistMix = prevPopularArtistMixDef.await(),
                    prevOldGem = prevOldGemDef.await(),

                    suggestedArtist = suggestedArtist,
                    suggestedAlbum = suggestedAlbumDef.await(),
                    suggestedArtistSong = suggestedArtistSongDef.await()
                ),
                playlist = playlistDef.await(),
                album = albumDef.await(),
                artist = artistDef.await()
            )
        }
    }

    override suspend fun getRefreshData(payload: ReqUserPayload, oldData: OldRefresh): DtoRefresh? {
        val user = db.getUserByEmail(payload.email, payload.userType) ?: return null
        if (user.bDate == null) return null

        return coroutineScope {
            val prevPopularSongMixDef = async {
                db.getPrevPopularCountrySong(user.id, user.countryId, oldData.oldMostPopularSong)
            }
            val prevPopularArtistMixDef = async {
                db.getPrevPopularArtistMix(user.id, oldData.oldPopularArtistSongs)
            }
            val prevOldGemDef = async {
                db.getPrevPopularYearSongs(user.id, user.bDate!!.year, oldData.oldPopularYearSongs)
            }

            val suggestedArtisteDef = async { db.getSuggestedArtist(user.id, user.countryId) }
            val suggestedAlbumDef = async { db.getSuggestedAlbum(user.id) }

            val suggestedArtist = suggestedArtisteDef.await()
            val suggestedArtistSongDef = async {
                db.getSuggestedArtistSong(
                    userId = user.id,
                    suggestedArtistIdList = suggestedArtist.map { it.id }
                )
            }

            DtoRefresh(
                prevPopularSongMix = prevPopularSongMixDef.await(),
                prevPopularArtistMix = prevPopularArtistMixDef.await(),
                prevOldGem = prevOldGemDef.await(),

                suggestedArtist = suggestedArtist,
                suggestedAlbum = suggestedAlbumDef.await(),
                suggestedArtistSong = suggestedArtistSongDef.await()
            )
        }
    }

    override suspend fun getAddSongToPlaylistData(
        payload: ReqUserPayload,
    ): List<DtoAddSongToPlaylistPageItem>? = coroutineScope {
        val user = db.getUserByEmail(payload.email, payload.userType) ?: return@coroutineScope null

        val favouriteDef = async {
            db.getYourFavouriteSongToAddToPlaylist(user.id).toDtoAddSongToPlaylistPageItem(
                type = DtoAddSongToPlaylistPageType.YOUR_FAVOURITES
            )
        }
        val suggestedDef = async {
            db.getSuggestedSongToAddToPlaylist().toDtoAddSongToPlaylistPageItem(
                type = DtoAddSongToPlaylistPageType.SUGGESTED_FOR_YOU
            )
        }
        val youMayAlsoLikeDef = async {
            db.getYouMayAlsoLikeSongToAddToPlaylist(user.countryId).toDtoAddSongToPlaylistPageItem(
                type = DtoAddSongToPlaylistPageType.YOU_MAY_ALSO_LIKE
            )
        }

        val fev = favouriteDef.await()
        val suggest = suggestedDef.await()
        val youMayLike = youMayAlsoLikeDef.await()

        val finalFev = if (fev.data.size < 20) DtoAddSongToPlaylistPageItem(
            type = fev.type,
            data = (suggest.data.shuffled(Random) + youMayLike.data.shuffled(Random))
                .shuffled(Random)
                .take(
                    Random.nextInt(15, 20)
                ) + fev.data
        ) else fev

        listOf(finalFev, suggest, youMayLike)
    }
}
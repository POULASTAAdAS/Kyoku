package com.poulastaa.data.repository.home

import com.poulastaa.data.model.db_table.user_artist.PasskeyUserArtistRelationTable
import com.poulastaa.data.model.home.HomeReq
import com.poulastaa.data.model.home.HomeResponse
import com.poulastaa.data.model.home.HomeResponseStatus
import com.poulastaa.data.model.home.HomeType
import com.poulastaa.data.model.utils.DbUsers
import com.poulastaa.data.model.utils.UserTypeHelper
import com.poulastaa.domain.dao.user_artist.PasskeyUserArtistRelation
import com.poulastaa.domain.repository.album.AlbumRepository
import com.poulastaa.domain.repository.aritst.ArtistRepository
import com.poulastaa.domain.repository.genre.GenreRepository
import com.poulastaa.domain.repository.home.HomeRepository
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.plugins.dbQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    private val dbUsers: DbUsers,
    private val song: SongRepository,
    private val album: AlbumRepository,
    private val artist: ArtistRepository,
    private val genre: GenreRepository,
    private val playlist: PlaylistRepository,
) : HomeRepository {
    override suspend fun generateHomeResponse(
        req: HomeReq,
        helper: UserTypeHelper // id is user.id
    ): HomeResponse = withContext(Dispatchers.IO) {
        when (req.type) {
            HomeType.NEW_USER_REQ -> {
                val artistIdList = dbQuery {
                    PasskeyUserArtistRelation.find {
                        PasskeyUserArtistRelationTable.userId eq 1
                    }.map {
                        it.artistId
                    }
                }

                HomeResponse(
                    status = HomeResponseStatus.SUCCESS,
                    type = HomeType.NEW_USER_REQ,
                    fevArtistsMix = async { artist.getArtistMixPreview(helper) }.await(),
                    album = async { album.getResponseAlbumPreview(artistIdList) }.await(),
                )
            }

            HomeType.DALY_REFRESH_REQ -> {
                HomeResponse(
                    status = HomeResponseStatus.SUCCESS,
                    type = HomeType.DALY_REFRESH_REQ,
                    fevArtistsMix = async { artist.getArtistMixPreview(helper) }.await(),
                )
            }

            HomeType.ALREADY_USER_REQ -> HomeResponse()
        }
    }
}

package com.poulastaa.data.repository.user_db

import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.auth_response.HomeResponse
import com.poulastaa.data.model.auth.auth_response.HomeResponseStatus
import com.poulastaa.data.model.auth.auth_response.HomeType
import com.poulastaa.data.model.auth.google.GoogleAuthResponse
import com.poulastaa.data.model.db_table.user.GoogleAuthUserTable
import com.poulastaa.domain.dao.user.GoogleAuthUser
import com.poulastaa.domain.repository.login.LogInResponseRepository
import com.poulastaa.domain.repository.user_db.GoogleAuthUserRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toGoogleAuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GoogleAuthUserRepositoryImpl(
    private val loginRepository: LogInResponseRepository
) : GoogleAuthUserRepository {
    private suspend fun findUser(email: String): GoogleAuthUser? = dbQuery {
        GoogleAuthUser.find {
            GoogleAuthUserTable.email eq email
        }.firstOrNull()
    }

    override suspend fun createOrLoginUser(
        userName: String,
        email: String,
        sub: String,
        pictureUrl: String,
        countryId: Int
    ): GoogleAuthResponse {
        try {
            val user = findUser(email)

            if (user == null) {
                val newUser = dbQuery {
                    GoogleAuthUser.new {
                        this.userName = userName
                        this.sub = sub
                        this.email = email
                        this.profilePicUrl = pictureUrl
                        this.countryId = countryId
                    }
                }

                return newUser.toGoogleAuthResponse(status = UserCreationStatus.CREATED) // signup
            }

            return user.toGoogleAuthResponse(  // login
                status = UserCreationStatus.CONFLICT,
                homeResponse = getHomeResponse(
                    userId = user.id.value,
                    userType = UserType.GOOGLE_USER
                )
            )
        } catch (e: Exception) {
            return GoogleAuthResponse(status = UserCreationStatus.SOMETHING_WENT_WRONG)
        }
    }

    private suspend fun getHomeResponse(userId: Long, userType: UserType) = withContext(Dispatchers.IO) {
        val getFevArtistMixDeferred = async {
            loginRepository.getFevArtistMix(userId, userType)
        }

        val getAlbumPrevDeferred = async {
            loginRepository.getAlbumPrev(userId, userType)
        }

        val getArtistPrevDeferred = async {
            loginRepository.getArtistPrev(userId, userType)
        }

        val getDailyMixPrevDeferred = async {
            loginRepository.getDailyMixPrev(userId, userType)
        }

        val getHistoryPrevDeferred = async {
            loginRepository.getHistoryPrev(userId, userType)
        }

        val getAlbumsDeferred = async {
            loginRepository.getAlbums(userId, userType)
        }

        val getPlaylistsDeferred = async {
            loginRepository.getPlaylists(userId, userType)
        }

        val getFavouritesDeferred = async {
            loginRepository.getFavourites(userId, userType)
        }


        HomeResponse(
            status = HomeResponseStatus.SUCCESS,
            type = HomeType.ALREADY_USER_REQ,

            fevArtistsMixPreview = getFevArtistMixDeferred.await(),
            albumPreview = getAlbumPrevDeferred.await(),
            artistsPreview = getArtistPrevDeferred.await(),
            dailyMixPreview = getDailyMixPrevDeferred.await(),

            albums = getAlbumsDeferred.await(),
            playlist = getPlaylistsDeferred.await(),
            favourites = getFavouritesDeferred.await(),
            historyPreview = getHistoryPrevDeferred.await()
        )
    }
}
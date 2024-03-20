package com.poulastaa.data.repository.user_db

import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.auth_response.HomeResponse
import com.poulastaa.data.model.auth.auth_response.HomeResponseStatus
import com.poulastaa.data.model.auth.auth_response.HomeType
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import com.poulastaa.domain.dao.user.PasskeyAuthUser
import com.poulastaa.domain.repository.login.LogInResponseRepository
import com.poulastaa.domain.repository.user_db.PasskeyAuthUserRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toPasskeyAuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class PasskeyAuthUserRepositoryImpl(
    private val loginRepository: LogInResponseRepository
) : PasskeyAuthUserRepository {
    override suspend fun findUserByEmail(email: String): PasskeyAuthUser? = dbQuery {
        PasskeyAuthUser.find {
            PasskeyAuthUserTable.email eq email
        }.firstOrNull()
    }

    override suspend fun createUser(
        userId: String,
        email: String,
        userName: String,
        profilePic: String,
        countryId: Int
    ): PasskeyAuthResponse {
        val user = findUserByEmail(email)

        if (user == null) {
            val newUser = dbQuery {
                PasskeyAuthUser.new {
                    this.userId = userId
                    this.email = email
                    this.displayName = userName
                    this.profilePic = profilePic
                    this.countryId = countryId
                }
            }

            return newUser.toPasskeyAuthResponse(status = UserCreationStatus.CREATED)
        }

        return PasskeyAuthResponse(
            status = UserCreationStatus.CONFLICT
        )
    }

    override suspend fun loginUser(userId: String): Pair<String, PasskeyAuthResponse> {
        val user = findUserByUserId(userId) ?: return Pair(
            first = "",
            second = PasskeyAuthResponse(
                status = UserCreationStatus.USER_NOT_FOUND
            )
        )

        return Pair(
            first = user.email,
            second = user.toPasskeyAuthResponse(
                status = UserCreationStatus.CONFLICT,
                homeResponse = getHomeResponse(
                    userId = user.id.value,
                    userType = UserType.PASSKEY_USER
                )
            )
        )
    }


    private suspend fun findUserByUserId(userId: String) = dbQuery {
        PasskeyAuthUser.find {
            PasskeyAuthUserTable.userId eq userId
        }.firstOrNull()
    }

    private suspend fun getHomeResponse(userId: Long, userType: UserType) = withContext(Dispatchers.IO) {
        val isOldEnoughDeferred = async {
            loginRepository.isOldEnough(userId, userType)
        }

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
            isOldEnough = isOldEnoughDeferred.await(),
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
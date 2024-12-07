package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mindrot.jbcrypt.BCrypt

class AuthenticationService(
    private val db: LocalAuthDatasource,
) : AuthRepository {
    override suspend fun googleAuth(
        payload: GoogleAuthPayloadDto,
        countryCode: String,
    ): AuthResponseDto = coroutineScope {
        val countryIdDef = async { db.getCountryId(countryCode) }
        val userDef = async { db.getUsersByEmail(payload.email, UserType.GOOGLE) }

        val countryId = countryIdDef.await() ?: return@coroutineScope AuthResponseDto()
        val user = userDef.await()

        if (user != null &&
            !verifyPassword(payload.sub, user.passwordHash)
        ) return@coroutineScope AuthResponseDto(
            status = AuthResponseStatusDto.PASSWORD_DOES_NOT_MATCH
        )

        when {
            user == null -> {
                val passHash = payload.sub.encryptPassword() ?: return@coroutineScope AuthResponseDto()

                val user = db.createGoogleUser(
                    user = ServerUserDto(
                        email = payload.email,
                        username = payload.userName,
                        password = passHash,
                        profilePicUrl = payload.profilePicUrl,
                        countryId = countryId,
                    )
                )

                AuthResponseDto(
                    status = AuthResponseStatusDto.USER_CREATED,
                    user = UserDto(
                        email = user.email,
                        username = user.userName,
                        profilePicUrl = user.profilePicUrl,
                    )
                )
            }

            user.bDate == null -> AuthResponseDto(
                status = AuthResponseStatusDto.USER_FOUND_STORE_B_DATE,
                user = UserDto(
                    email = user.email,
                    username = user.userName,
                    profilePicUrl = user.profilePicUrl,
                )
            )

            else -> AuthResponseDto(
                status = AuthResponseStatusDto.USER_FOUND,
                user = UserDto(
                    email = user.email,
                    username = user.userName,
                    profilePicUrl = user.profilePicUrl,
                )
            )
        }
    }

    private fun String.encryptPassword(): String? = BCrypt.hashpw(this, BCrypt.gensalt(10))
    private fun verifyPassword(inputPassword: String, storedHash: String) = BCrypt.checkpw(inputPassword, storedHash)
}
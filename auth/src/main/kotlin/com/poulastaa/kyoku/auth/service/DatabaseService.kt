package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.database.entity.EntityJWTToken
import com.poulastaa.kyoku.auth.database.entity.EntityUser
import com.poulastaa.kyoku.auth.database.repository.CountryDataSource
import com.poulastaa.kyoku.auth.database.repository.UserDataSource
import com.poulastaa.kyoku.auth.database.repository.UserJWTTokenDataSource
import com.poulastaa.kyoku.auth.database.repository.UserTypeDataSource
import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.model.dto.UserType
import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.JWTToken
import com.poulastaa.kyoku.auth.utils.UserId
import com.poulastaa.kyoku.grpc.user_core.CoreUserServiceGrpc
import com.poulastaa.kyoku.grpc.user_core.GRPCRequestUpdatePassword
import com.poulastaa.kyoku.grpc.user_core.GRPCRequestUser
import com.poulastaa.kyoku.grpc.user_core.GRPCUserType
import net.devh.boot.grpc.client.inject.GrpcClient
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@Service
@Transactional
class DatabaseService(
    private val user: UserDataSource,
    private val jwt: UserJWTTokenDataSource,
    private val userType: UserTypeDataSource,
) {
    @GrpcClient("user")
    private lateinit var grpcCoreUser: CoreUserServiceGrpc.CoreUserServiceFutureStub
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getUserByEmailAndType(
        email: Email,
        type: UserType,
    ) = user.getEntityUserByEmailAndUserTypeId(
        email = email,
        userTypeId = getUserTypeByType(type).id
    )?.toDtoUse()

    fun createUser(newUser: DtoUser) = try {
        val user = grpcCoreUser.withDeadlineAfter(30, TimeUnit.SECONDS).createUser(
            GRPCRequestUser.newBuilder().apply {
                this.username = newUser.username
                this.displayName = newUser.displayName
                this.email = newUser.email
                this.passwordHash = newUser.passwordHash
                newUser.profileUrl?.let { this.profileUrl = it }
                newUser.birthDate?.let { this.dateOfBrith = it.toString() }
                this.type = GRPCUserType.valueOf(newUser.type.toString())
                this.countryCode = newUser.countryCode
            }.build()
        ).get()

        DtoUser(
            id = user.userId,
            username = user.username,
            email = user.email,
            type = UserType.valueOf(newUser.type.toString()),
            displayName = newUser.displayName,
            passwordHash = user.passwordHash,
            countryCode = user.countryCode,
            profileUrl = user.profileUrl,
            birthDate = if (user.dateOfBrith.isNotBlank()) LocalDate.parse(user.dateOfBrith) else null
        )
    } catch (e: Exception) {
        logger.error(e.message, e)
        null
    }

    fun updateRefreshToken(id: UserId, refreshToken: JWTToken) {
        val entry = jwt.findById(id).orElse(null)
        if (entry != null) {
            entry.refreshToken = refreshToken
            jwt.save(entry)
        } else {
            jwt.save(
                EntityJWTToken().apply {
                    this.id = id
                    this.refreshToken = refreshToken
                }
            )
        }
    }

    fun updatePassword(
        id: UserId,
        passwordHash: String,
    ) = if (user.existsById(id)) {
        try {
            grpcCoreUser.updatePassword(GRPCRequestUpdatePassword.newBuilder().apply {
                this.userId = id
                this.passwordHash = passwordHash
            }.build())
            true
        } catch (e: Exception) {
            logger.error(e.message, e)
            false
        }
    } else null

    private fun getUserTypeByType(type: UserType) = userType.findByTypeIgnoreCase(type.name)

    private fun EntityUser.toDtoUse() = DtoUser(
        id = this.id,
        username = this.username,
        displayName = this.displayName,
        email = this.email,
        passwordHash = this.passwordHash,
        countryCode = this.country.code,
        type = UserType.valueOf(this.userType.type),
        profileUrl = this.profilePicUrl,
        birthDate = this.birthDate?.toLocalDate()
    )
}
package com.poulastaa.kyoku.user.service

import com.poulastaa.kyoku.grpc.user_core.CoreUserServiceGrpc
import com.poulastaa.kyoku.grpc.user_core.EmptyResponse
import com.poulastaa.kyoku.grpc.user_core.GRPCRequestUpdatePassword
import com.poulastaa.kyoku.grpc.user_core.GRPCRequestUser
import com.poulastaa.kyoku.grpc.user_core.GRPCResponseUser
import com.poulastaa.kyoku.grpc.user_core.GRPCUserType
import com.poulastaa.kyoku.user.database.entity.EntityCountry
import com.poulastaa.kyoku.user.database.entity.EntityUser
import com.poulastaa.kyoku.user.database.repository.CountryDataSource
import com.poulastaa.kyoku.user.database.repository.UserDataSource
import com.poulastaa.kyoku.user.database.repository.UserTypeDataSource
import com.poulastaa.kyoku.user.domain.model.dto.UserType
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.sql.Date
import java.time.LocalDate

@GrpcService
class CoreUserService(
    private val user: UserDataSource,
    private val userType: UserTypeDataSource,
    private val country: CountryDataSource,
) : CoreUserServiceGrpc.CoreUserServiceImplBase() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun createUser(
        request: GRPCRequestUser,
        responseObserver: StreamObserver<GRPCResponseUser>,
    ) {
        try {
            val responseUser = user.save(
                EntityUser().apply {
                    this.username = request.username
                    this.displayName = request.displayName
                    this.email = request.email
                    this.passwordHash = request.passwordHash
                    this.profilePicUrl = request.profileUrl
                    this.country = getCountryByCode(request.countryCode)
                    this.userType = getUserTypeByType(UserType.valueOf(request.type.name))

                    if (request.dateOfBrith.isNotBlank() && request.dateOfBrith.isNotEmpty())
                        this.birthDate = Date.valueOf(LocalDate.parse(request.dateOfBrith))
                }
            ).let { entityUser ->
                GRPCResponseUser.newBuilder().apply {
                    this.userId = entityUser.id
                    this.username = entityUser.username
                    this.displayName = entityUser.displayName
                    this.email = entityUser.email
                    this.passwordHash = entityUser.passwordHash
                    this.type = request.type
                    this.countryCode = entityUser.country.code
                    entityUser.profilePicUrl?.let { this.profileUrl = it }
                    entityUser.birthDate?.let { this.dateOfBrith = it.toString() }
                }.build()
            }

            responseObserver.onNext(responseUser)
            logger.info("user created on email: ${request.email}")
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error(e.message, e)
            responseObserver.onError(e)
        }
    }

    override fun updatePassword(
        request: GRPCRequestUpdatePassword,
        responseObserver: StreamObserver<EmptyResponse>,
    ) {
        try {
            user.getReferenceById(request.userId).passwordHash = request.passwordHash

            responseObserver.onNext(EmptyResponse.newBuilder().build())
            responseObserver.onCompleted()
        } catch (e: EntityNotFoundException) {
            logger.error(e.message, e)
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("User not found")
                    .asRuntimeException()
            )
        } catch (e: Exception) {
            logger.error(e.message, e)
            responseObserver.onError(e)
        }
    }

    private fun getUserTypeByType(type: UserType) = userType.findByTypeIgnoreCase(type.name)
    private fun getCountryByCode(code: String) = country.getEntityCountryByCodeIgnoreCase(code)
}
package com.poulastaa.kyoku.user.service

import com.poulastaa.kyoku.grpc.playlistUser.PlaylistUserServiceGrpc
import com.poulastaa.kyoku.grpc.playlistUser.RequestUser
import com.poulastaa.kyoku.grpc.playlistUser.RequestUserId
import com.poulastaa.kyoku.grpc.playlistUser.ResponseUserId
import com.poulastaa.kyoku.user.database.user.repository.UserDataSource
import com.poulastaa.kyoku.user.database.user.repository.UserTypeDataSource
import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@GrpcService
class GRPCPlaylistUserService(
    private val user: UserDataSource,
    private val userType: UserTypeDataSource,
) : PlaylistUserServiceGrpc.PlaylistUserServiceImplBase() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    override fun getUserId(
        request: RequestUserId,
        responseObserver: StreamObserver<ResponseUserId>,
    ) {
        try {
            val type = when (request.user.type) {
                RequestUser.UserType.EMAIL,
                RequestUser.UserType.GOOGLE,
                    -> request.user.type.name

                else -> {
                    responseObserver.onError(
                        Status.INVALID_ARGUMENT.withDescription("UserType not allowed").asRuntimeException()
                    )
                    return
                }
            }.let { userType.findByTypeIgnoreCase(it) } ?: run {
                responseObserver.onError(Status.NOT_FOUND.withDescription("UserType not found").asRuntimeException())
                return
            }

            val user = user.findByUserTypeAndEmail(
                userType = type,
                email = request.user.email
            ) ?: run {
                responseObserver.onError(Status.NOT_FOUND.withDescription("User not found").asRuntimeException())
                return
            }

            responseObserver.onNext(ResponseUserId.newBuilder().setUserId(user.id).build())
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error("getUserId failed", e)
            responseObserver.onError(
                Status.INTERNAL.withDescription("Failed to get user id").withCause(e).asRuntimeException()
            )
        }
    }
}

package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.grpc.model.RequestUser
import com.poulastaa.kyoku.grpc.playlist_user.PlaylistUserServiceGrpc
import com.poulastaa.kyoku.grpc.playlist_user.RequestUserId
import com.poulastaa.kyoku.playlist.domain.model.ImportPlaylistException
import com.poulastaa.kyoku.playlist.utils.UserId
import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

private const val USER_DEADLINE_SECONDS = 3L

@Component
class UserClient {
    @GrpcClient("user")
    private lateinit var userStub: PlaylistUserServiceGrpc.PlaylistUserServiceBlockingStub

    fun getUserId(user: RequestUser): UserId = try {
        userStub.withDeadlineAfter(USER_DEADLINE_SECONDS, TimeUnit.SECONDS)
            .getUserId(RequestUserId.newBuilder().setUser(user).build())
            .userId
    } catch (e: StatusRuntimeException) {
        throw when (e.status.code) {
            Status.Code.NOT_FOUND,
            Status.Code.INVALID_ARGUMENT,
                -> ImportPlaylistException(Status.NOT_FOUND, "User not found", e)

            else -> ImportPlaylistException(Status.UNAVAILABLE, "User service unavailable", e)
        }
    }
}

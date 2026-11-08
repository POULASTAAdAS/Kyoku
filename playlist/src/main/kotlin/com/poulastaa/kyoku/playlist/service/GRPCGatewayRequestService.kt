package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.grpc.gateway_playlist.*
import com.poulastaa.kyoku.playlist.domain.model.ImportPlaylistException
import com.poulastaa.kyoku.playlist.service.mapper.toResponsePlaylist
import com.poulastaa.kyoku.playlist.service.mapper.toResponseSong
import com.poulastaa.kyoku.playlist.utils.DebugUtils
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

/**
 * gRPC layer only: validate → delegate → map result / failure. Always terminates the observer.
 * Runs blocking on the gRPC executor so transactions, cancellation and errors stay on one call path.
 */
@GrpcService
class GRPCGatewayRequestService(
    private val importService: ImportPlaylistService,
    private val userPlaylistService: UserPlaylistService,
) : GatewayPlaylistServiceGrpc.GatewayPlaylistServiceImplBase() {
    override fun importPlaylist(
        request: RequestImportPlaylist,
        responseObserver: StreamObserver<ResponseFullPlaylist>,
    ) {
        if (request.spotifyPlaylistId.isBlank() || request.user.email.isBlank()) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT.withDescription("spotifyPlaylistId and user are required").asRuntimeException()
            )
            return
        }

        try {
            val result = importService.import(request.spotifyPlaylistId.trim(), request.user, request.force)

            responseObserver.onNext(result.toResponseFullPlaylist())
            responseObserver.onCompleted()
        } catch (e: ImportPlaylistException) {
            DebugUtils.d("import playlist ${request.spotifyPlaylistId} rejected: ${e.message}")
            responseObserver.onError(e.status.withDescription(e.message).withCause(e).asRuntimeException())
        } catch (e: StatusRuntimeException) {
            DebugUtils.e("import playlist ${request.spotifyPlaylistId} failed on downstream call: ${e.status}")
            responseObserver.onError(
                Status.UNAVAILABLE.withDescription("Dependent service unavailable").withCause(e).asRuntimeException()
            )
        } catch (e: Exception) {
            DebugUtils.e("import playlist ${request.spotifyPlaylistId} failed: ${e.message}")
            responseObserver.onError(
                Status.INTERNAL.withDescription("Failed to import playlist").withCause(e).asRuntimeException()
            )
        }
    }

    override fun getUserPlaylists(
        request: RequestUserPlaylists,
        responseObserver: StreamObserver<ResponseUserPlaylists>,
    ) {
        if (request.user.email.isBlank()) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT.withDescription("user is required").asRuntimeException()
            )
            return
        }

        try {
            val result = userPlaylistService.getPlaylists(request.user)

            responseObserver.onNext(
                ResponseUserPlaylists.newBuilder()
                    .addAllPlaylists(result.map { it.toResponseFullPlaylist() })
                    .build()
            )
            responseObserver.onCompleted()
        } catch (e: ImportPlaylistException) {
            DebugUtils.d("get user playlists rejected: ${e.message}")
            responseObserver.onError(e.status.withDescription(e.message).withCause(e).asRuntimeException())
        } catch (e: StatusRuntimeException) {
            DebugUtils.e("get user playlists failed on downstream call: ${e.status}")
            responseObserver.onError(
                Status.UNAVAILABLE.withDescription("Dependent service unavailable").withCause(e).asRuntimeException()
            )
        } catch (e: Exception) {
            DebugUtils.e("get user playlists failed: ${e.message}")
            responseObserver.onError(
                Status.INTERNAL.withDescription("Failed to get playlists").withCause(e).asRuntimeException()
            )
        }
    }

    private fun ImportedPlaylist.toResponseFullPlaylist(): ResponseFullPlaylist =
        ResponseFullPlaylist.newBuilder().apply {
            playlist = this@toResponseFullPlaylist.playlist.toResponsePlaylist()
            addAllSongs(songs.map { it.toResponseSong() })
        }.build()
}

package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.grpc.gateway_playlist.GatewayPlaylistServiceGrpc
import com.poulastaa.kyoku.grpc.gateway_playlist.RequestImportPlaylist
import com.poulastaa.kyoku.grpc.gateway_playlist.ResponseFullPlaylist
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

            responseObserver.onNext(
                ResponseFullPlaylist.newBuilder().apply {
                    playlist = result.playlist.toResponsePlaylist()
                    addAllSongs(result.songs.map { it.toResponseSong() })
                }.build()
            )
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
}

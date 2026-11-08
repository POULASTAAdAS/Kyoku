package com.poulastaa.kyoku.content.service.grpc

import com.poulastaa.kyoku.content.service.song.SongStreamService
import com.poulastaa.kyoku.grpc.gateway_song.GatewaySongServiceGrpc
import com.poulastaa.kyoku.grpc.gateway_song.RequestSongStreamUrl
import com.poulastaa.kyoku.grpc.gateway_song.ResponseSongStreamUrl
import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcSongService(
    private val service: SongStreamService,
) : GatewaySongServiceGrpc.GatewaySongServiceImplBase() {
    override fun getStreamUrl(
        request: RequestSongStreamUrl,
        responseObserver: StreamObserver<ResponseSongStreamUrl>,
    ) {
        if (request.songId <= 0 || request.email.isBlank()) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT.withDescription("songId and email are required").asRuntimeException()
            )
            return
        }

        try {
            val signed = service.getStreamUrl(songId = request.songId, email = request.email)

            responseObserver.onNext(
                ResponseSongStreamUrl.newBuilder().apply {
                    this.url = signed.url
                    this.expiresAtSec = signed.expiresAtSec
                }.build()
            )

            responseObserver.onCompleted()
        } catch (e: SongStreamService.SongNotFoundException) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.message).asRuntimeException())
        } catch (e: Exception) {
            e.printStackTrace()

            responseObserver.onError(
                Status.INTERNAL.withDescription(e.message ?: "something went wrong").asRuntimeException()
            )
        }
    }
}

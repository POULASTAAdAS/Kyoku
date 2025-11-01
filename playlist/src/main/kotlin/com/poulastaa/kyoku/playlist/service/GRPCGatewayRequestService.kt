package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.grpc.gateway_playlist.GatewayPlaylistServiceGrpc
import com.poulastaa.kyoku.grpc.gateway_playlist.RequestGetPlaylist
import com.poulastaa.kyoku.grpc.gateway_playlist.ResponseFullPlaylist
import io.grpc.stub.StreamObserver
import org.springframework.grpc.client.interceptor.security.BearerTokenAuthenticationInterceptor
import org.springframework.grpc.server.service.GrpcService

@GrpcService
class GRPCGatewayRequestService : GatewayPlaylistServiceGrpc.GatewayPlaylistServiceImplBase() {
    override fun getPlaylist(
        request: RequestGetPlaylist,
        responseObserver: StreamObserver<ResponseFullPlaylist>,
    ) {
        println("djaidjwijdwi")
        print(request)
    }
}
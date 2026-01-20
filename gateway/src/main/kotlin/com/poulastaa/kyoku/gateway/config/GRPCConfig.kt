package com.poulastaa.kyoku.gateway.config

import com.poulastaa.kyoku.grpc.gateway_playlist.GatewayPlaylistServiceGrpc
import net.devh.boot.grpc.client.inject.StubTransformer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

//@Configuration
//class GRPCConfig {
//    @Bean
//    fun stubTransformer(): StubTransformer {
//        return StubTransformer { _, stub ->
//            if (stub is GatewayPlaylistServiceGrpc.GatewayPlaylistServiceFutureStub)
//                stub.withDeadlineAfter(10, TimeUnit.SECONDS)
//            else stub
//        }
//    }
//}
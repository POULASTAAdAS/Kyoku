package com.poulastaa.kyoku.auth.config

import com.poulastaa.kyoku.grpc.user_core.CoreUserServiceGrpc
import net.devh.boot.grpc.client.inject.StubTransformer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class GRPCConfig {
    @Bean
    fun stubTransformer(): StubTransformer {
        return StubTransformer { _, stub ->
            if (stub is CoreUserServiceGrpc.CoreUserServiceFutureStub)
                stub.withDeadlineAfter(30, TimeUnit.SECONDS)
            else stub
        }
    }
}
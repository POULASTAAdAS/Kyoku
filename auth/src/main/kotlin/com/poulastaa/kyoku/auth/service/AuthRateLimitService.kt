package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.model.AuthRateLimitPolicy
import com.poulastaa.kyoku.auth.model.dto.DtoRateLimitResult
import io.github.bucket4j.distributed.proxy.ProxyManager
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class AuthRateLimitService(
    private val proxyManager: ProxyManager<String>,
) {
    fun consume(policy: AuthRateLimitPolicy): DtoRateLimitResult {
        val probe = proxyManager.builder()
            .build(policy.cacheKey) { policy.configuration() }
            .tryConsumeAndReturnRemaining(1)

        return DtoRateLimitResult(
            consumed = probe.isConsumed,
            remainingTokens = probe.remainingTokens,
            retryAfterSeconds = if (probe.isConsumed) 0 else probe.nanosToWaitForRefill
                .let { nanos -> (nanos - 1) / TimeUnit.SECONDS.toNanos(1) + 1 }
                .coerceAtLeast(1),
        )
    }
}

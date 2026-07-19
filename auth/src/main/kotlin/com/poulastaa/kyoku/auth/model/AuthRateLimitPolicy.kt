package com.poulastaa.kyoku.auth.model

import io.github.bucket4j.BucketConfiguration
import org.springframework.http.HttpMethod
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

enum class AuthRateLimitPolicy(
    val method: HttpMethod,
    path: String,
    val capacity: Long,
    private val refill: Refill,
) {
    EMAIL_SIGN_IN(
        HttpMethod.POST,
        Endpoints.EMAIL_SING_IN,
        3,
        Refill.ONE_TOKEN_INTERVAL
    ),
    EMAIL_SIGN_UP(
        HttpMethod.POST,
        Endpoints.EMAIL_SING_UP,
        3,
        Refill.ONE_TOKEN_INTERVAL
    ),
    VERIFY_EMAIL(
        HttpMethod.GET,
        Endpoints.VERIFY_EMAIL,
        5,
        Refill.MINUTE_BOUNDARY
    ),
    CHECK_VERIFICATION_MAIL_STATE(
        HttpMethod.GET,
        Endpoints.CHECK_VERIFICATION_MAIL_STATE,
        12,
        Refill.MINUTE_BOUNDARY,
    ),
    FORGOT_PASSWORD(
        HttpMethod.GET,
        Endpoints.FORGOT_PASSWORD,
        3,
        Refill.ONE_TOKEN_INTERVAL,
    ),
    VALIDATE_PASSWORD_OTP(
        HttpMethod.GET,
        Endpoints.VALIDATE_PASSWORD_OTP,
        3,
        Refill.ONE_TOKEN_INTERVAL,
    ),
    RESET_PASSWORD(
        HttpMethod.POST,
        Endpoints.RESET_PASSWORD,
        3,
        Refill.ONE_TOKEN_INTERVAL,
    ),
    GOOGLE_AUTH(
        HttpMethod.POST,
        Endpoints.GOOGLE_AUTH,
        3,
        Refill.ONE_TOKEN_INTERVAL,
    );

    val requestPath = "/$path"
    val cacheKey = "auth:rate-limit:$name"

    fun configuration(now: Instant = Instant.now()): BucketConfiguration = BucketConfiguration.builder()
        .addLimit { limit ->
            when (refill) {
                Refill.ONE_TOKEN_INTERVAL -> limit
                    .capacity(capacity)
                    .refillIntervally(1, Duration.ofSeconds(45))

                Refill.MINUTE_BOUNDARY -> limit
                    .capacity(capacity)
                    .refillIntervallyAligned(
                        capacity,
                        Duration.ofMinutes(1),
                        now.truncatedTo(ChronoUnit.MINUTES).plus(1, ChronoUnit.MINUTES),
                    )
            }
        }
        .build()

    companion object {
        private val policiesByRequest = entries.associateBy { it.method.name() to it.requestPath }

        fun from(method: String, path: String) = policiesByRequest[method to path]
    }

    private enum class Refill {
        ONE_TOKEN_INTERVAL,
        MINUTE_BOUNDARY,
    }
}

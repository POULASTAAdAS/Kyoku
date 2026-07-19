package com.poulastaa.kyoku.auth.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.auth.model.AuthRateLimitPolicy
import com.poulastaa.kyoku.auth.model.response.ResponseStatus
import com.poulastaa.kyoku.auth.model.response.ResponseWrapper
import com.poulastaa.kyoku.auth.service.AuthRateLimitService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthRateLimitConfig(
    private val interceptor: AuthRateLimitInterceptor,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptor)
            .addPathPatterns(AuthRateLimitPolicy.entries.map(AuthRateLimitPolicy::requestPath))
    }
}

@Component
class AuthRateLimitInterceptor(
    private val service: AuthRateLimitService,
    private val objectMapper: ObjectMapper,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val requestPath = request.requestURI.removePrefix(request.contextPath)
        val policy = AuthRateLimitPolicy.from(request.method, requestPath) ?: return true
        val result = service.consume(policy)

        response.setHeader(RATE_LIMIT_LIMIT_HEADER, policy.capacity.toString())
        response.setHeader(RATE_LIMIT_REMAINING_HEADER, result.remainingTokens.toString())
        if (result.consumed) return true

        response.status = ResponseStatus.TOO_MANY_REQUESTS.code.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.setHeader(RETRY_AFTER_HEADER, result.retryAfterSeconds.toString())
        objectMapper.writeValue(
            response.outputStream,
            ResponseWrapper<Unit>(
                status = ResponseStatus.TOO_MANY_REQUESTS,
                message = ResponseStatus.TOO_MANY_REQUESTS.message,
                code = ResponseStatus.TOO_MANY_REQUESTS.code.value(),
            )
        )
        return false
    }

    companion object {
        const val RATE_LIMIT_LIMIT_HEADER = "X-Kyoku-RateLimit-Limit"
        const val RATE_LIMIT_REMAINING_HEADER = "X-Kyoku-RateLimit-Remaining"
        const val RETRY_AFTER_HEADER = "Retry-After"
    }
}

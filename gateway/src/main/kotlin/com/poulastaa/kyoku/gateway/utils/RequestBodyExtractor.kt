package com.poulastaa.kyoku.gateway.utils

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

object RequestBodyExtractor {

    /**
     * Extracts the request body as a String using Spring's recommended approach.
     *
     * This method:
     * 1. Uses DataBufferUtils.join() to combine multiple buffers
     * 2. Properly releases buffers to prevent memory leaks
     * 3. Uses flatMap instead of map for proper reactive chaining
     * 4. Handles charset encoding explicitly
     *
     * @param exchange The ServerWebExchange containing the request
     * @return The request body as a String
     * @throws IllegalStateException if the body is empty or cannot be read
     */
    suspend fun extractBodyAsString(exchange: ServerWebExchange) = DataBufferUtils.join(exchange.request.body)
        .flatMap { dataBuffer ->
            try {
                val bytes = ByteArray(dataBuffer.readableByteCount())
                dataBuffer.read(bytes)
                Mono.just(String(bytes, StandardCharsets.UTF_8))
            } finally {
                // Always release buffer in finally block to prevent leaks
                DataBufferUtils.release(dataBuffer)
            }
        }
        .switchIfEmpty(Mono.error(IllegalArgumentException("Request body is empty")))
        .awaitSingle()!!
}
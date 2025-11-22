package com.poulastaa.kyoku.gateway.utils

import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec

/**
 * This function removes duplicate CORS (Cross-Origin Resource Sharing) headers from HTTP responses.
 * When multiple services or filters add CORS headers, duplicates can occur which may cause issues with browsers are removed.
 *
 * Uses Spring Cloud Gateway's dedupeResponseHeader() method with the "RETAIN_FIRST" strategy:
 *
 *      -> Access-Control-Allow-Origin - specifies allowed origins
 *      -> Access-Control-Allow-Credentials - indicates if credentials are allowed
 *      -> Access-Control-Allow-Methods - specifies allowed HTTP methods
 *      -> Access-Control-Allow-Headers - specifies allowed request headers
 *      -> Access-Control-Max-Age - specifies how long preflight results can be cached
 *      -> Access-Control-Expose-Headers - specifies headers that can be exposed to the client
 *      -> Vary - indicates which headers affect caching
 */
fun GatewayFilterSpec.dedupeAllCorsHeaders() = this
    .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST")
    .dedupeResponseHeader("Access-Control-Allow-Credentials", "RETAIN_FIRST")
    .dedupeResponseHeader("Access-Control-Allow-Methods", "RETAIN_FIRST")
    .dedupeResponseHeader("Access-Control-Allow-Headers", "RETAIN_FIRST")
    .dedupeResponseHeader("Access-Control-Max-Age", "RETAIN_FIRST")
    .dedupeResponseHeader("Access-Control-Expose-Headers", "RETAIN_FIRST")
    .dedupeResponseHeader("Vary", "RETAIN_FIRST")!!
package com.poulastaa.kyoku.playlist.domain.model

import io.grpc.Status

/**
 * Expected import failure carrying the gRPC status returned to the gateway.
 */
class ImportPlaylistException(
    val status: Status,
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

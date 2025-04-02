package com.poulastaa.search.utils

import com.poulastaa.search.model.PagingParams
import io.ktor.server.routing.*

internal fun RoutingCall.getPagingParams(): PagingParams? {
    val size = this.parameters["size"]?.toIntOrNull() ?: return null
    val page = this.parameters["page"]?.toIntOrNull() ?: return null
    val query = this.parameters["query"]

    return PagingParams(
        page = page,
        size = size,
        query = query
    )
}
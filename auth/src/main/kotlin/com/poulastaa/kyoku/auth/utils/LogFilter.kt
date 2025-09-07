package com.poulastaa.kyoku.auth.utils

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.turbo.TurboFilter
import ch.qos.logback.core.spi.FilterReply
import org.slf4j.Marker

class LogFilter : TurboFilter() {
    override fun decide(
        marker: Marker?,
        logger: Logger?,
        level: Level?,
        format: String?,
        params: Array<out Any?>?,
        t: Throwable?,
    ): FilterReply {
        if (marker?.name?.contains("http-outgoing") == true) return FilterReply.DENY
        else if (marker?.name?.contains("endpoint lease request") == true) return FilterReply.DENY
        else if (marker?.name?.contains("kyoku.poulastaa.shop:8001") == true) return FilterReply.DENY
        else if (marker?.name?.contains("ex-") == true) return FilterReply.DENY

        return FilterReply.NEUTRAL
    }
}
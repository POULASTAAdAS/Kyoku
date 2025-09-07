package com.poulastaa.kyoku.gateway.utils

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
    ) = if (format?.contains("eureka") == true) FilterReply.DENY
    else if (format?.contains("docker") == true) FilterReply.DENY
    else if (format == "Accept=[application/json, application/*+json]") FilterReply.DENY
    else FilterReply.NEUTRAL
}
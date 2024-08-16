package com.poulastaa.data.mappers

import com.poulastaa.data.model.PinnedType
import com.poulastaa.domain.model.route_model.req.pin.PinReqType
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


fun Long.getYear(): Int {
    val instant = Instant.ofEpochMilli(this)
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    var bYear = dateTime.year
    val currentYear = LocalDate.now().year

    val diffInYear = currentYear - bYear

    if (diffInYear < 15) return currentYear - 4 // if age < 15

    bYear += 13 // ingress year by 13

    return bYear
}

fun PinReqType.toPinnedType() = when (this) {
    PinReqType.PLAYLIST -> PinnedType.PLAYLIST
    PinReqType.ARTIST -> PinnedType.ARTIST
    PinReqType.ALBUM -> PinnedType.ALBUM
    PinReqType.FAVOURITE -> PinnedType.FAVOURITE
}
package com.poulastaa.setup.presentation.set_b_date

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toDateString(): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}
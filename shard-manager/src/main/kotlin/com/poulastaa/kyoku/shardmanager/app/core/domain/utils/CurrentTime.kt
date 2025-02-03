package com.poulastaa.kyoku.shardmanager.app.core.domain.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter

val CURRENT_TIME = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
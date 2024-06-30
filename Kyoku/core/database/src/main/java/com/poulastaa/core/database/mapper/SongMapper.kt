package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.model.SongColorResult
import com.poulastaa.core.domain.model.SongColor

fun SongColorResult.toSongColor() = SongColor(
    primary = primary,
    background = background,
    onBackground = onBackground
)
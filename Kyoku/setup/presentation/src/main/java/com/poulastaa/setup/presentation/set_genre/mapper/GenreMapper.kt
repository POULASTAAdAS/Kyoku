package com.poulastaa.setup.presentation.set_genre.mapper

import com.poulastaa.core.domain.model.Genre
import com.poulastaa.setup.presentation.set_genre.model.UiGenre

fun Genre.toUiGenre() = UiGenre(
    id = id,
    name = name
)
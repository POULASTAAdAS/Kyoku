package com.poulastaa.setup.presentation.pic_genre

import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.GenreId

fun DtoGenre.toUiGenre(list: List<GenreId>) = UiGenre(
    id = this.id,
    name = this.name,
    poster = this.cover,
    isSelected = list.any { it == this.id }
)
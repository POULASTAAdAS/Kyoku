package com.poulastaa.kyoku.content.utils

import com.poulastaa.kyoku.content.database.entity.EntityGenre
import com.poulastaa.kyoku.content.model.dto.DtoGenre

fun EntityGenre.toDtoGenre() = DtoGenre(
    id = this.id,
    type = this.name,
    rawPoster = this.coverImage,
    popularity = this.popularity
)
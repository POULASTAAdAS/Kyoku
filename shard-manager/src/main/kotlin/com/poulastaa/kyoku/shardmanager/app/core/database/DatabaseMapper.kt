package com.poulastaa.kyoku.shardmanager.app.core.database

import com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku.DaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.suggestion.ShardDaoSong
import com.poulastaa.kyoku.shardmanager.app.core.domain.model.DtoDBArtist
import com.poulastaa.kyoku.shardmanager.app.core.domain.model.DtoDbSong

fun DaoArtist.toDbArtistDto() = DtoDBArtist(
    id = this.id.value,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity
)
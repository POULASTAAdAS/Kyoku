package com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.suggestion

import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardSuggestionEntitySong
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ShardDaoSong(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ShardDaoSong>(ShardSuggestionEntitySong)

    var popularity by ShardSuggestionEntitySong.popularity
}
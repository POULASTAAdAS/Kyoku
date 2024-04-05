package com.poulastaa.kyoku.data.model.database.table.internal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.poulastaa.kyoku.data.model.api.service.pinned.IdType
import com.poulastaa.kyoku.data.model.api.service.pinned.PinnedOperation

@Entity(
    tableName = "InternalPinnedTable"
)
data class InternalPinnedTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pinnedId: Long = 0,
    val type: IdType = IdType.ERR,
    val operation: PinnedOperation = PinnedOperation.ERR
)

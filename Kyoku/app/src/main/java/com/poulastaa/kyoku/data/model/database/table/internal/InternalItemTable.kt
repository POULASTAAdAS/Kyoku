package com.poulastaa.kyoku.data.model.database.table.internal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.poulastaa.kyoku.data.model.api.service.item.ItemOperation
import com.poulastaa.kyoku.data.model.api.service.pinned.IdType

@Entity(
    tableName = "InternalItemTable"
)
data class InternalItemTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: Long = 0,
    val type: IdType = IdType.ERR,
    val operation: ItemOperation = ItemOperation.ERR
)

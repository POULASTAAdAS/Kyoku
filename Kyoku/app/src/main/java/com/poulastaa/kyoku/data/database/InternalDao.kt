package com.poulastaa.kyoku.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.kyoku.data.model.api.service.item.ItemOperation
import com.poulastaa.kyoku.data.model.api.service.pinned.IdType
import com.poulastaa.kyoku.data.model.api.service.pinned.PinnedOperation
import com.poulastaa.kyoku.data.model.database.table.internal.InternalItemTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalPinnedTable

@Dao
interface InternalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToPinnedTable(entry: InternalPinnedTable)

    @Query("select id from InternalPinnedTable where pinnedId = :pinnedId and type = :type and operation = :operation limit 1")
    suspend fun checkIfPinnedIdPresent(
        pinnedId: Long,
        type: IdType,
        operation: PinnedOperation = PinnedOperation.ADD
    ): Long?

    @Query("delete from InternalPinnedTable where pinnedId = :pinnedId and type = :type and operation = :operation")
    suspend fun removeFromPinnedTable(
        pinnedId: Long,
        type: IdType,
        operation: PinnedOperation
    )


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTopItemTable(entry: InternalItemTable)


    // todo may change
    @Query("select id from InternalItemTable where itemId = :id and type = :type and operation = :operation")
    suspend fun checkIfItemIdPresent(
        id: Long,
        type: IdType,
        operation: ItemOperation = ItemOperation.ADD
    ): Long?

    @Query("delete from InternalItemTable where itemId = :id and type = :type and operation = :operation")
    suspend fun removeFromItemTable(
        id: Long,
        type: IdType,
        operation: ItemOperation
    )
}
package com.poulastaa.kyoku.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.kyoku.data.model.api.service.pinned.IdType
import com.poulastaa.kyoku.data.model.api.service.pinned.PinnedOperation
import com.poulastaa.kyoku.data.model.database.table.InternalPinnedTable

@Dao
interface InternalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToPinnedTable(entry: InternalPinnedTable)

    @Query("select id from InternalPinnedTable where pinnedId = :pinnedId and type = :type and operation = :operation limit 1")
    suspend fun checkIfPresent(
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
}
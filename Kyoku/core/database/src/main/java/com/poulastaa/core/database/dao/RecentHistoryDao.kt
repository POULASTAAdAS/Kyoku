package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.core.database.entity.RecentHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: RecentHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entries: List<RecentHistoryEntity>)

    @Query("select * from RecentHistoryEntity order by timeStamp desc limit 10")
    fun getAll(): Flow<List<RecentHistoryEntity>>

    @Query(
        """
        delete from RecentHistoryEntity where timeStamp not in (
            select timeStamp from RecentHistoryEntity order by timeStamp desc limit 10
        )
    """
    )
    suspend fun deleteOldEntry()

    @Query("select * from RecentHistoryEntity where otherId != 'INDIVIDUAL' order by timeStamp desc limit 1")
    suspend fun getLastOtherEntry(): RecentHistoryEntity?
}
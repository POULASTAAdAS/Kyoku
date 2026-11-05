package com.poulastaa.common.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.poulastaa.common.database.dao.AuthDao
import com.poulastaa.common.database.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal const val DATABASE_NAME = "kyoku.db"

@Database(
    entities = [UserEntity::class],
    version = 1,
)
@ConstructedBy(KyokuDatabaseConstructor::class)
abstract class KyokuDatabase : RoomDatabase() {
    abstract val authDao: AuthDao
}

// Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object KyokuDatabaseConstructor : RoomDatabaseConstructor<KyokuDatabase> {
    override fun initialize(): KyokuDatabase
}

internal fun RoomDatabase.Builder<KyokuDatabase>.buildKyokuDatabase(): KyokuDatabase =
    setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(dropAllTables = true)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

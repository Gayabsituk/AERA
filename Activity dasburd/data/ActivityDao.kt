package com.example.activity.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<ActivityEntry>>

    @Query("SELECT * FROM activity_entries WHERE timestamp >= :startOfDay ORDER BY timestamp DESC")
    fun getTodayEntries(startOfDay: Long): Flow<List<ActivityEntry>>

    @Insert
    suspend fun insert(entry: ActivityEntry)

    @Query("DELETE FROM activity_entries WHERE id = :id")
    suspend fun deleteById(id: Long)
}

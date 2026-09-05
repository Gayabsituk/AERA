package com.example.activity.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ActivityCategory {
    TRANSPORT, ENERGY, FOOD, SHOPPING, WASTE
}

@Entity(tableName = "activity_entries")
data class ActivityEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: ActivityCategory,
    val type: String,
    val value: Double, // Main value (distance, kwh, amount, items, weight)
    val footprint: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val trips: Int = 1,
    val passengers: Int = 1
)

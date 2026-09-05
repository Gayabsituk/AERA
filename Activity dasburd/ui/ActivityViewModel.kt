package com.example.activity.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.activity.data.ActivityCategory
import com.example.activity.data.ActivityDatabase
import com.example.activity.data.ActivityEntry
import com.example.activity.logic.FootprintCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class ActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ActivityDatabase.getDatabase(application).activityDao()

    val allEntries: StateFlow<List<ActivityEntry>> = dao.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val todayEntries: StateFlow<List<ActivityEntry>> = dao.getTodayEntries(getStartOfDay())
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun logActivity(
        category: ActivityCategory,
        type: String,
        value: Double,
        trips: Int = 1,
        passengers: Int = 1
    ) {
        viewModelScope.launch {
            val footprint = FootprintCalculator.calculate(category, type, value, trips, passengers)
            val entry = ActivityEntry(
                category = category,
                type = type,
                value = value,
                footprint = footprint,
                trips = trips,
                passengers = passengers
            )
            dao.insert(entry)
        }
    }

    fun deleteActivity(id: Long) {
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }

    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

package com.bikedisplay.storage

import com.bikedisplay.domain.RideTelemetry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppSettings(
    val useMetricUnits: Boolean = true,
    val theme: String = "System",
    val autoReconnect: Boolean = true
)

interface AppSettingsStore {
    val settings: StateFlow<AppSettings>
    suspend fun updateSettings(settings: AppSettings)
}

interface RideHistoryStore {
    suspend fun recordTelemetry(telemetry: RideTelemetry)
    suspend fun loadRecent(limit: Int): List<RideTelemetry>
}

class InMemoryAppSettingsStore : AppSettingsStore {
    private val mutableSettings = MutableStateFlow(AppSettings())
    override val settings: StateFlow<AppSettings> = mutableSettings.asStateFlow()

    override suspend fun updateSettings(settings: AppSettings) {
        mutableSettings.value = settings
    }
}

class InMemoryRideHistoryStore : RideHistoryStore {
    private val history = mutableListOf<RideTelemetry>()

    override suspend fun recordTelemetry(telemetry: RideTelemetry) {
        history += telemetry
    }

    override suspend fun loadRecent(limit: Int): List<RideTelemetry> {
        return history.takeLast(limit)
    }
}

package com.bikedisplay.companion

import com.bikedisplay.bluetooth.BluetoothSessionManager
import com.bikedisplay.bluetooth.BluetoothSessionState
import com.bikedisplay.domain.BikeGoFeature
import com.bikedisplay.domain.BikeGoFeatureMatrix
import com.bikedisplay.domain.NavigationInstruction
import com.bikedisplay.domain.NavigationPipeline
import com.bikedisplay.domain.RideTelemetry
import com.bikedisplay.storage.AppSettings
import com.bikedisplay.storage.InMemoryAppSettingsStore
import com.bikedisplay.storage.InMemoryRideHistoryStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CompanionAppCoordinator {
    private val featureMatrix = BikeGoFeatureMatrix.default()
    private val navigationPipeline = NavigationPipeline()
    private val settingsStore = InMemoryAppSettingsStore()
    private val rideHistoryStore = InMemoryRideHistoryStore()
    private val bluetoothSessionManager = BluetoothSessionManager()

    private val mutableState = MutableStateFlow(
        AppState(
            telemetry = RideTelemetry(),
            nextInstruction = navigationPipeline.currentInstruction,
            supportedFeatures = featureMatrix.supportedFeatures(),
            appSettings = settingsStore.settings.value,
            bluetoothState = bluetoothSessionManager.state.value
        )
    )

    val state: StateFlow<AppState> = mutableState.asStateFlow()

    fun onTelemetryUpdate(telemetry: RideTelemetry) {
        mutableState.update { it.copy(telemetry = telemetry) }
    }

    suspend fun recordTelemetry(telemetry: RideTelemetry) {
        rideHistoryStore.recordTelemetry(telemetry)
        onTelemetryUpdate(telemetry)
    }

    fun onNavigationUpdate(instruction: NavigationInstruction) {
        navigationPipeline.updateInstruction(instruction)
        mutableState.update { it.copy(nextInstruction = instruction) }
    }

    suspend fun onSettingsUpdate(settings: AppSettings) {
        settingsStore.updateSettings(settings)
        mutableState.update { it.copy(appSettings = settings) }
    }

    fun onDiscovered(address: String) {
        bluetoothSessionManager.onDeviceDiscovered(address)
        mutableState.update { it.copy(bluetoothState = bluetoothSessionManager.state.value) }
    }

    fun onConnected(address: String) {
        bluetoothSessionManager.onConnected(address)
        mutableState.update { it.copy(bluetoothState = bluetoothSessionManager.state.value) }
    }

    fun onDisconnected(willReconnect: Boolean) {
        bluetoothSessionManager.onDisconnected(willReconnect)
        mutableState.update { it.copy(bluetoothState = bluetoothSessionManager.state.value) }
    }
}

data class AppState(
    val telemetry: RideTelemetry,
    val nextInstruction: NavigationInstruction,
    val supportedFeatures: List<BikeGoFeature>,
    val appSettings: AppSettings,
    val bluetoothState: BluetoothSessionState
)

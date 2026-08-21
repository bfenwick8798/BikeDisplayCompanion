package com.bikedisplay.companion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BikeDisplayDashboard(viewModel)
                }
            }
        }
    }
}

class MainViewModel : ViewModel() {
    private val featureMatrix = BikeGoFeatureMatrix.default()
    private val navigationPipeline = NavigationPipeline()
    private val settingsStore = InMemoryAppSettingsStore()
    private val rideHistoryStore = InMemoryRideHistoryStore()

    private val _state = MutableStateFlow(
        UiState(
            telemetry = RideTelemetry(),
            nextInstruction = navigationPipeline.currentInstruction,
            supportedFeatures = featureMatrix.supportedFeatures(),
            appSettings = settingsStore.settings.value
        )
    )

    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsStore.settings.collect { settings ->
                _state.update { current -> current.copy(appSettings = settings) }
            }
        }
    }

    fun onTelemetryUpdate(telemetry: RideTelemetry) {
        _state.update { it.copy(telemetry = telemetry) }
        viewModelScope.launch {
            rideHistoryStore.recordTelemetry(telemetry)
        }
    }

    fun onNavigationUpdate(instruction: NavigationInstruction) {
        navigationPipeline.updateInstruction(instruction)
        _state.update { it.copy(nextInstruction = instruction) }
    }
}

data class UiState(
    val telemetry: RideTelemetry,
    val nextInstruction: NavigationInstruction,
    val supportedFeatures: List<BikeGoFeature>,
    val appSettings: AppSettings
)

@Composable
private fun BikeDisplayDashboard(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("BikeDisplayCompanion", style = MaterialTheme.typography.headlineSmall)
        Text("Speed: ${state.telemetry.speedKmh} km/h")
        Text("Battery: ${state.telemetry.batteryPercent}%")
        Text("Assist Level: ${state.telemetry.assistLevel}")
        Text("Next turn: ${state.nextInstruction.maneuver}")
        Text("Distance: ${state.nextInstruction.distanceMeters}m")
        Text("Theme: ${state.appSettings.theme}")
        Text("Supported features: ${state.supportedFeatures.joinToString { it.name }}")
    }
}

package com.bikedisplay.bluetooth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BluetoothSessionManager {
    private val mutableState = MutableStateFlow<BluetoothSessionState>(BluetoothSessionState.Disconnected)
    val state: StateFlow<BluetoothSessionState> = mutableState.asStateFlow()

    fun onDeviceDiscovered(deviceAddress: String) {
        mutableState.value = BluetoothSessionState.Discovered(deviceAddress)
    }

    fun onConnected(deviceAddress: String) {
        mutableState.value = BluetoothSessionState.Connected(deviceAddress)
    }

    fun onDisconnected(willReconnect: Boolean) {
        mutableState.value =
            if (willReconnect) BluetoothSessionState.Reconnecting else BluetoothSessionState.Disconnected
    }

    fun reset() {
        mutableState.value = BluetoothSessionState.Disconnected
    }
}

sealed interface BluetoothSessionState {
    data object Disconnected : BluetoothSessionState
    data object Reconnecting : BluetoothSessionState
    data class Discovered(val deviceAddress: String) : BluetoothSessionState
    data class Connected(val deviceAddress: String) : BluetoothSessionState
}

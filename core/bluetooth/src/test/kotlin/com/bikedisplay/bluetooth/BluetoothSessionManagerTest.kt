package com.bikedisplay.bluetooth

import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothSessionManagerTest {
    @Test
    fun `reconnect state after disconnect`() {
        val manager = BluetoothSessionManager()
        manager.onConnected("AA:BB")
        manager.onDisconnected(willReconnect = true)

        assertEquals(BluetoothSessionState.Reconnecting, manager.state.value)
    }
}

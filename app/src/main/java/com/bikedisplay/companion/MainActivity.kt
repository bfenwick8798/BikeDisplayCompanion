package com.bikedisplay.companion

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val coordinator = CompanionAppCoordinator()
        val state = coordinator.state.value
        val textView = TextView(this).apply {
            text = "BikeDisplayCompanion\n" +
                "Speed: ${state.telemetry.speedKmh} km/h\n" +
                "Battery: ${state.telemetry.batteryPercent}%\n" +
                "Next turn: ${state.nextInstruction.maneuver}"
            textSize = 18f
            setPadding(32, 48, 32, 32)
        }
        setContentView(textView)
    }
}

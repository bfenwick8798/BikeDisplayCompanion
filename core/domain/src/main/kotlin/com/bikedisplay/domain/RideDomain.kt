package com.bikedisplay.domain

data class RideTelemetry(
    val speedKmh: Double = 0.0,
    val batteryPercent: Int = 100,
    val assistLevel: Int = 0,
    val cadenceRpm: Int = 0,
    val rangeKm: Int = 0,
    val tripDistanceKm: Double = 0.0
)

data class NavigationInstruction(
    val maneuver: String = "Continue",
    val distanceMeters: Int = 0,
    val etaMinutes: Int = 0,
    val alert: String? = null
)

class NavigationPipeline {
    var currentInstruction: NavigationInstruction = NavigationInstruction()
        private set

    fun updateInstruction(instruction: NavigationInstruction) {
        currentInstruction = instruction
    }

    fun needsReroute(offRouteMeters: Int): Boolean = offRouteMeters >= 30
}

enum class BikeGoFeature {
    TURN_BY_TURN_NAVIGATION,
    DASHBOARD,
    ASSIST_CONTROL,
    BATTERY_MONITORING,
    RIDE_HISTORY,
    OFFLINE_ASSETS,
    THEMES,
    NOTIFICATIONS
}

class BikeGoFeatureMatrix(private val matrix: Map<BikeGoFeature, FeaturePhase>) {
    fun supportedFeatures(): List<BikeGoFeature> =
        matrix.filterValues { it != FeaturePhase.NOT_PLANNED }.keys.toList()

    fun phase(feature: BikeGoFeature): FeaturePhase = matrix[feature] ?: FeaturePhase.NOT_PLANNED

    companion object {
        fun default() = BikeGoFeatureMatrix(
            mapOf(
                BikeGoFeature.TURN_BY_TURN_NAVIGATION to FeaturePhase.MUST_HAVE,
                BikeGoFeature.DASHBOARD to FeaturePhase.MUST_HAVE,
                BikeGoFeature.ASSIST_CONTROL to FeaturePhase.MUST_HAVE,
                BikeGoFeature.BATTERY_MONITORING to FeaturePhase.MUST_HAVE,
                BikeGoFeature.RIDE_HISTORY to FeaturePhase.PHASE_TWO,
                BikeGoFeature.OFFLINE_ASSETS to FeaturePhase.PHASE_TWO,
                BikeGoFeature.THEMES to FeaturePhase.PHASE_TWO,
                BikeGoFeature.NOTIFICATIONS to FeaturePhase.MUST_HAVE
            )
        )
    }
}

enum class FeaturePhase {
    MUST_HAVE,
    PHASE_TWO,
    NOT_PLANNED
}

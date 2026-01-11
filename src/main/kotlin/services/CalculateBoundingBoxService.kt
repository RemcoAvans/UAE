package com.example.services

import kotlin.math.PI
import kotlin.math.cos

object CalculateBoundingBoxService {
    data class GeoBoundingBox(
        val minLat: Double,
        val maxLat: Double,
        val minLon: Double,
        val maxLon: Double
    )

    fun calculateBoundingBox(
        latitude: Double,
        longitude: Double,
        radiusInMeters: Double
    ): GeoBoundingBox {

        val earthRadius = 6378137.0 // meters (WGS84)

        // Latitude: meters naar graden
        val latDelta = radiusInMeters / earthRadius * (180 / PI)

        // Longitude: meters naar graden (afhankelijk van latitude)
        val lonDelta = radiusInMeters / (earthRadius * cos(latitude * PI / 180)) * (180 / PI)

        return GeoBoundingBox(
            minLat = latitude - latDelta,
            maxLat = latitude + latDelta,
            minLon = longitude - lonDelta,
            maxLon = longitude + lonDelta
        )
    }
}
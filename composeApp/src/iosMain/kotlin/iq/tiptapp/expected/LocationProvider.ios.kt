package iq.tiptapp.expected

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController
import platform.CoreLocation.kCLLocationAccuracyBest


actual fun getPlatformLocationProvider(controller: PermissionsController): LocationTracker {
    return LocationTracker(controller, kCLLocationAccuracyBest)
}
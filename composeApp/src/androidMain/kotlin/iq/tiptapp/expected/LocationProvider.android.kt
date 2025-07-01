package iq.tiptapp.expected

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController

actual fun getPlatformLocationProvider(controller: PermissionsController): LocationTracker {
    return LocationTracker(controller)
}
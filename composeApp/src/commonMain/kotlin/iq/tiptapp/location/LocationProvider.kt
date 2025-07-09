package iq.tiptapp.location

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController

expect fun getPlatformLocationProvider(controller: PermissionsController): LocationTracker
package iq.tiptapp.expected

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController

expect fun getPlatformLocationProvider(controller: PermissionsController): LocationTracker
package iq.tiptapp.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import iq.tiptapp.LocalNativeMapViewFactory

@Composable
actual fun MapComponent(
    location: Pair<Double, Double>?,
    onMarkerTapped: (Double, Double) -> Unit
) {
    val factory = LocalNativeMapViewFactory.current
    UIKitViewController(
        modifier = Modifier.fillMaxSize(),
        factory = {
            factory.createGoogleMapView(location?.first, location?.second) { lat, lng ->
                onMarkerTapped(lat, lng)
            }
        }
    )
}

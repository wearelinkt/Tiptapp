package iq.tiptapp.expected

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import iq.tiptapp.LocalNativeMapViewFactory

@Composable
actual fun MapComponent(
    location: Pair<Double, Double>?,
    markerSnippet: String,
    onMarkerTapped: (Double, Double) -> Unit
) {
    val factory = LocalNativeMapViewFactory.current
    UIKitViewController(
        modifier = Modifier.fillMaxSize(),
        factory = {
            factory.createGoogleMapView(location?.first, location?.second, markerSnippet) { lat, lng ->
                onMarkerTapped(lat, lng)
            }
        }
    )
}

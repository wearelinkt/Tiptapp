package iq.tiptapp.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import iq.tiptapp.LocalNativeViewFactory

@Composable
actual fun MapComponent(onMarkerTapped: (Double, Double) -> Unit) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = Modifier.fillMaxSize(),
        factory = {
            factory.createGoogleMapView { lat, lng ->
                onMarkerTapped(lat, lng)
            }
        }
    )
}

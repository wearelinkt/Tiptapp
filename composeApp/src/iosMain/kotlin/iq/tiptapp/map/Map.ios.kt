package iq.tiptapp.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import iq.tiptapp.LocalNativeViewFactory
import iq.tiptapp.ui.help.MapViewModel

@Composable
actual fun MapComponent(viewModel: MapViewModel) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = Modifier.fillMaxSize(),
        factory = {
            factory.createGoogleMapView { lat, lng ->
                viewModel.onMarkerClicked(lat, lng)
            }
        }
    )
}

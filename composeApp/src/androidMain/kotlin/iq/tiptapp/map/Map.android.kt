package iq.tiptapp.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import iq.tiptapp.ui.help.HelpViewModel

@Composable
actual fun MapComponent(viewModel: HelpViewModel) {
    var markerPosition by remember { mutableStateOf(LatLng(35.6997, 51.3380)) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 15f)
    }

    val markerState = remember(markerPosition) { MarkerState(position = markerPosition) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerPosition = latLng // ✅ Update state
            viewModel.onMarkerClicked(latLng.latitude, latLng.longitude)
        }
    ) {
        Marker(
            state = markerState,
            title = "Selected Location",
            snippet = "Lat: ${markerPosition.latitude}, Lng: ${markerPosition.longitude}",
            onClick = {
                viewModel.onMarkerClicked(it.position.latitude, it.position.longitude)
                false // Show info window
            }
        )
    }
}


package iq.tiptapp.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun MapComponent(
    location: Pair<Double, Double>?,
    markerSnippet: String, onMarkerTapped: (Double, Double) -> Unit
) {

    val initialPosition = location?.let {
        LatLng(it.first, it.second)
    } ?: run {
        LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 15f)
    }

    // Marker will be at the center of the map
    val markerState = remember { MarkerState(position = initialPosition) }

    // Update marker as camera moves
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position.target }
            .collect { newCenter ->
                markerState.position = newCenter
                onMarkerTapped(newCenter.latitude, newCenter.longitude)
            }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            // Move camera to tapped location
            cameraPositionState.move(
                CameraUpdateFactory.newLatLng(latLng)
            )
            onMarkerTapped(latLng.latitude, latLng.longitude)
        }
    ) {
        Marker(
            state = markerState,
            title = "Selected Location",
            snippet = markerSnippet,
            onClick = {
                onMarkerTapped(it.position.latitude, it.position.longitude)
                false // Show info window
            }
        )
    }
}

private const val DEFAULT_LATITUDE = 33.2232
private const val DEFAULT_LONGITUDE = 43.6793


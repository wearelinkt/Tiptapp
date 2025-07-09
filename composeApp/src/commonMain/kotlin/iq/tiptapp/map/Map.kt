package iq.tiptapp.map

import androidx.compose.runtime.Composable

@Composable
expect fun MapComponent(
    location: Pair<Double, Double>?,
    markerSnippet: String,
    onMarkerTapped: (Double, Double) -> Unit
)
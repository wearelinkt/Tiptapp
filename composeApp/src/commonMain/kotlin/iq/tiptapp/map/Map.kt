package iq.tiptapp.map

import androidx.compose.runtime.Composable

@Composable
expect fun MapComponent(
    location: Pair<Double, Double>?,
    onMarkerTapped: (Double, Double) -> Unit
)
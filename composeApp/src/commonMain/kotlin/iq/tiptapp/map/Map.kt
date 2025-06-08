package iq.tiptapp.map

import androidx.compose.runtime.Composable

@Composable
expect fun MapComponent(onMarkerTapped: (Double, Double) -> Unit)
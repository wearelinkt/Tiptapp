package iq.tiptapp.expected

import androidx.compose.runtime.Composable

@Composable
expect fun SearchComponent(onPlaceSelected: (Double, Double) -> Unit, onDone: () -> Unit)
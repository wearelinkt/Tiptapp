package iq.tiptapp.search

import androidx.compose.runtime.Composable

@Composable
expect fun SearchComponent(onPlaceSelected: (Double, Double) -> Unit, onDone: () -> Unit)
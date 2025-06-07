package iq.tiptapp.map

import androidx.compose.runtime.Composable
import iq.tiptapp.ui.help.MapViewModel

@Composable
expect fun MapComponent(viewModel: MapViewModel)
package iq.tiptapp.ui.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import iq.tiptapp.map.MapComponent

@Composable
fun MapScreen(viewModel: MapViewModel) {
    Scaffold {
        Column(Modifier.fillMaxSize()) {
            MapComponent(viewModel)
        }
    }
}
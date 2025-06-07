package iq.tiptapp.ui.help

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel : ViewModel() {
    private val _clickedLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val clickedLocation: StateFlow<Pair<Double, Double>?> = _clickedLocation

    fun onMarkerClicked(lat: Double, lng: Double) {
        _clickedLocation.value = lat to lng
    }
}
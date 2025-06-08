package iq.tiptapp.ui.help

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HelpViewModel : ViewModel() {
    private val _pickUpClickedLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val pickUpClickedLocation: StateFlow<Pair<Double, Double>?> = _pickUpClickedLocation

    private val _dropOffClickedLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val dropOffClickedLocation: StateFlow<Pair<Double, Double>?> = _dropOffClickedLocation

    fun onMarkerPickUpClicked(lat: Double, lng: Double) {
        _pickUpClickedLocation.value = lat to lng
    }

    fun onMarkerDropOffClicked(lat: Double, lng: Double) {
        _dropOffClickedLocation.value = lat to lng
    }
}
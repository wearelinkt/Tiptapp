package iq.tiptapp.expected

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class HelpViewModel : ViewModel() {

    private val _pickUpClickedLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    actual val pickUpClickedLocation: StateFlow<Pair<Double, Double>?> = _pickUpClickedLocation

    private val _dropOffClickedLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    actual val dropOffClickedLocation: StateFlow<Pair<Double, Double>?> = _dropOffClickedLocation

    actual fun onMarkerPickUpClicked(lat: Double, lng: Double) {
        _pickUpClickedLocation.value = lat to lng
    }

    actual fun onMarkerDropOffClicked(lat: Double, lng: Double) {
        _dropOffClickedLocation.value = lat to lng
    }
}
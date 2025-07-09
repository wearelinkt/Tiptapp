package iq.tiptapp.help

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class HelpViewModel(
    private val savedStateHandle: SavedStateHandle
) : BaseHelpViewModel() {

    private val _pickUpClickedLocation = MutableStateFlow(
        savedStateHandle.get<Pair<Double, Double>?>(KEY_PICK_UP)
    )
    actual val pickUpClickedLocation: StateFlow<Pair<Double, Double>?> = _pickUpClickedLocation

    private val _dropOffClickedLocation = MutableStateFlow(
        savedStateHandle.get<Pair<Double, Double>?>(KEY_DROP_OFF)
    )
    actual val dropOffClickedLocation: StateFlow<Pair<Double, Double>?> = _dropOffClickedLocation

    actual fun onMarkerPickUpClicked(lat: Double, lng: Double) {
        val location = lat to lng
        _pickUpClickedLocation.value = location
        savedStateHandle[KEY_PICK_UP] = location
    }

    actual fun onMarkerDropOffClicked(lat: Double, lng: Double) {
        val location = lat to lng
        _dropOffClickedLocation.value = location
        savedStateHandle[KEY_DROP_OFF] = location
    }

    companion object {
        private const val KEY_PICK_UP = "pick_up_location"
        private const val KEY_DROP_OFF = "drop_off_location"
    }
}
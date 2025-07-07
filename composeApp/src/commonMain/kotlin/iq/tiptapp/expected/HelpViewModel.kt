package iq.tiptapp.expected

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

expect class HelpViewModel : ViewModel {
    val pickUpClickedLocation: StateFlow<Pair<Double, Double>?>
    val dropOffClickedLocation: StateFlow<Pair<Double, Double>?>

    fun onMarkerPickUpClicked(lat: Double, lng: Double)
    fun onMarkerDropOffClicked(lat: Double, lng: Double)
}
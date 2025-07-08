package iq.tiptapp.expected

import androidx.lifecycle.ViewModel
import iq.tiptapp.domain.model.DeliveryNavItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseHelpViewModel: ViewModel() {

    private val _pickUpDeliveryItem = MutableStateFlow<DeliveryNavItem?>(null)
    val pickUpDeliveryItem: StateFlow<DeliveryNavItem?> = _pickUpDeliveryItem

    private val _pickUpToggleState = MutableStateFlow(false)
    val pickUpToggleState: StateFlow<Boolean> = _pickUpToggleState

    private val _dropOffDeliveryItem = MutableStateFlow<DeliveryNavItem?>(null)
    val dropOffDeliveryItem: StateFlow<DeliveryNavItem?> = _dropOffDeliveryItem

    private val _dropOffToggleState = MutableStateFlow(false)
    val dropOffToggleState: StateFlow<Boolean> = _dropOffToggleState

    fun setPickUpDeliveryItem(item: DeliveryNavItem) {
        _pickUpDeliveryItem.value = item
    }

    fun setPickUpToggleState(state: Boolean) {
        _pickUpToggleState.value = state
    }

    fun setDropOffDeliveryItem(item: DeliveryNavItem) {
        _dropOffDeliveryItem.value = item
    }

    fun setDropOffToggleState(state: Boolean) {
        _dropOffToggleState.value = state
    }
}
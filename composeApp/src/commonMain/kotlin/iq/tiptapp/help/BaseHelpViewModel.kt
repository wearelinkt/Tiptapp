package iq.tiptapp.help

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

    private val _pickUpFloor = MutableStateFlow<Int?>(null)
    val pickUpFloor: StateFlow<Int?> = _pickUpFloor

    private val _pickUpDoorCode = MutableStateFlow<Int?>(null)
    val pickUpDoorCode: StateFlow<Int?> = _pickUpDoorCode

    private val _pickUpFitElevator = MutableStateFlow(false)
    val pickUpFitElevator: StateFlow<Boolean> = _pickUpFitElevator

    private val _pickUpInfo = MutableStateFlow<String?>(null)
    val pickUpInfo: StateFlow<String?> = _pickUpInfo

    private val _dropOffFloor = MutableStateFlow<Int?>(null)
    val dropOffFloor: StateFlow<Int?> = _dropOffFloor

    private val _dropOffDoorCode = MutableStateFlow<Int?>(null)
    val dropOffDoorCode: StateFlow<Int?> = _dropOffDoorCode

    private val _dropOffFitElevator = MutableStateFlow(false)
    val dropOffFitElevator: StateFlow<Boolean> = _dropOffFitElevator

    private val _dropOffInfo = MutableStateFlow<String?>(null)
    val dropOffInfo: StateFlow<String?> = _dropOffInfo

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

    fun setPickUpFloor(floor: Int) {
        _pickUpFloor.value = floor
    }

    fun setPickUpDoorCode(doorCode: Int) {
        _pickUpDoorCode.value = doorCode
    }

    fun setPickUpFitElevator(state: Boolean) {
        _pickUpFitElevator.value = state
    }

    fun setPickUpOtherInfo(info: String) {
        _pickUpInfo.value = info
    }

    fun setDropOffFloor(floor: Int) {
        _dropOffFloor.value = floor
    }

    fun setDropOffDoorCode(doorCode: Int) {
        _dropOffDoorCode.value = doorCode
    }

    fun setDropOffFitElevator(state: Boolean) {
        _dropOffFitElevator.value = state
    }

    fun setDropOffOtherInfo(info: String) {
        _dropOffInfo.value = info
    }

    fun reset() {
        _pickUpDeliveryItem.value = null
        _pickUpToggleState.value = false
        _pickUpFloor.value = null
        _pickUpDoorCode.value = null
        _pickUpFitElevator.value = false
        _pickUpInfo.value = null
        _dropOffDeliveryItem.value = null
        _dropOffToggleState.value = false
        _dropOffFloor.value = null
        _dropOffDoorCode.value = null
        _dropOffFitElevator.value = false
        _dropOffInfo.value = null
    }
}
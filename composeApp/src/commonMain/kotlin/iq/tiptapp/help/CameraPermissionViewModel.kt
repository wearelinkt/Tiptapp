package iq.tiptapp.help

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.StringResource

class CameraPermissionViewModel : ViewModel() {

    private val _cameraPermissionState = MutableStateFlow<PermissionState>(PermissionState.Dialog)
    val cameraPermissionState: StateFlow<PermissionState>
        get() = _cameraPermissionState

    private val _storagePermissionState = MutableStateFlow<PermissionState>(PermissionState.Dialog)
    val storagePermissionState: StateFlow<PermissionState>
        get() = _storagePermissionState

    fun setCameraState(state: PermissionState) {
        _cameraPermissionState.value = state
    }

    fun setStorageState(state: PermissionState) {
        _storagePermissionState.value = state
    }

    sealed class PermissionState {
        object Dialog : PermissionState()
        object Granted : PermissionState()
        data class Denied(val reason: StringResource) : PermissionState()
    }
}
package iq.tiptapp.ui.help

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


class LocationPermissionViewModel(
    private val controller: PermissionsController,
    private val locationTracker: LocationTracker
) : ViewModel() {

    var state by mutableStateOf(PermissionState.NotDetermined)
        private set

    private val _latLng = MutableStateFlow<LatLng?>(null)
    val latLng: StateFlow<LatLng?> = _latLng


    init {
        viewModelScope.launch {
            state = controller.getPermissionState(Permission.LOCATION)
            provideOrRequestLocationPermission()
        }
    }

    private suspend fun provideOrRequestLocationPermission() {
        try {
            controller.providePermission(Permission.LOCATION)
            state = PermissionState.Granted
        } catch (e: DeniedAlwaysException) {
            state = PermissionState.DeniedAlways
        } catch (e: DeniedException) {
            state = PermissionState.Denied
        } catch (e: RequestCanceledException) {
            e.printStackTrace()
        }
    }

    fun refreshLocation() {
        viewModelScope.launch {
            locationTracker.startTracking()
            locationTracker.getLocationsFlow()
                .distinctUntilChanged()
                .collect {
                    _latLng.tryEmit(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationTracker.stopTracking()
    }
}
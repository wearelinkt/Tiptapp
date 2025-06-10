package iq.tiptapp.ui.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import iq.tiptapp.location.getPlatformLocationProvider

@Composable
fun LocationScreen() {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }
    val locationTracker = getPlatformLocationProvider(controller)

    BindEffect(controller)
    BindLocationTrackerEffect(locationTracker)

    val viewModel = viewModel {
        LocationPermissionViewModel(controller, locationTracker)
    }


    val location = viewModel.latLng.collectAsState().value
    val permissionState = viewModel.state

    LaunchedEffect(permissionState) {
        if (permissionState == PermissionState.Granted) {
            viewModel.refreshLocation()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (permissionState) {
            PermissionState.Granted -> {
                Text("Permission granted.")
            }

            PermissionState.DeniedAlways -> {
                Text("Permission was permanently declined.")
                Button(onClick = {
                    controller.openAppSettings()
                }) {
                    Text("Open app settings")
                }
            }

            PermissionState.Denied -> {
                Text("Permission was declined. Please try again.")
            }

            else -> {
                Text("Requesting permission...")
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Lat=${location?.latitude}, Lng=${location?.longitude}")
    }
}
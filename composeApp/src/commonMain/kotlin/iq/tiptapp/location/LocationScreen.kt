package iq.tiptapp.location

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.github.aakira.napier.Napier
import iq.tiptapp.Turquoise
import iq.tiptapp.component.CustomTopAppBar
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.current_location
import tiptapp.composeapp.generated.resources.location_permission_denied
import tiptapp.composeapp.generated.resources.location_permission_denied_permanent
import tiptapp.composeapp.generated.resources.open_app_setting
import tiptapp.composeapp.generated.resources.search_address
import tiptapp.composeapp.generated.resources.search_for_address

@Composable
fun LocationScreen(
    title: StringResource,
    onBackClicked: () -> Unit,
    onAddressBoxClicked: () -> Unit,
    setupLatLngToNavigate: (Double, Double) -> Unit
) {
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
    val address = viewModel.address.collectAsState().value
    val permissionState = viewModel.state

    LaunchedEffect(permissionState) {
        if (permissionState == PermissionState.Granted) {
            viewModel.refreshLocation()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {
            CustomTopAppBar(title, onBackClicked)
            SearchAddressBox(onAddressBoxClicked, modifier = Modifier.padding(16.dp))

            when (permissionState) {
                PermissionState.Granted -> {
                    Napier.d("Permission granted.")
                }

                PermissionState.DeniedAlways -> {
                    Text(stringResource(Res.string.location_permission_denied_permanent))
                    Button(onClick = {
                        controller.openAppSettings()
                    }) {
                        Text(stringResource(Res.string.open_app_setting))
                    }
                }

                PermissionState.Denied -> {
                    Text(stringResource(Res.string.location_permission_denied))
                }

                else -> {
                    Napier.d("Requesting permission...")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                stringResource(Res.string.current_location),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .align(Alignment.Start)
            )
            CurrentLocationBox(
                address,
                location,
                setupLatLngToNavigate,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun SearchAddressBox(
    onAddressBoxClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                onAddressBoxClicked.invoke()
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(Res.string.search_address),
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Turquoise
        )
    }
}

@Composable
private fun CurrentLocationBox(
    address: String?,
    location: LatLng?,
    setupLatLngToNavigate: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            ).then(
                if (address == null) Modifier
                else Modifier.clickable {
                    location?.let { loc ->
                        setupLatLngToNavigate.invoke(loc.latitude, loc.longitude)
                    }
                }
            ).padding(16.dp)
    ) {
        Text(
            text = address ?: stringResource(Res.string.search_for_address),
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}
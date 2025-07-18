package iq.tiptapp.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import iq.tiptapp.Turquoise
import iq.tiptapp.component.CustomTopAppBar
import iq.tiptapp.help.HelpViewModel
import iq.tiptapp.utils.getAddress
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.address
import tiptapp.composeapp.generated.resources.carry
import tiptapp.composeapp.generated.resources.description
import tiptapp.composeapp.generated.resources.drop_off
import tiptapp.composeapp.generated.resources.no
import tiptapp.composeapp.generated.resources.pick_up
import tiptapp.composeapp.generated.resources.placement
import tiptapp.composeapp.generated.resources.publish_ad
import tiptapp.composeapp.generated.resources.title_text
import tiptapp.composeapp.generated.resources.yes

@Composable
fun PublishScreen(
    viewModel: HelpViewModel,
    onBackClicked: () -> Unit,
    titleClicked: () -> Unit,
    pickUpLocationClicked: () -> Unit,
    pickUpDeliveryClicked: () -> Unit,
    dropOffLocationClicked: () -> Unit,
    dropOffDeliveryClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomTopAppBar(Res.string.publish_ad, onBackClicked)
            InfoText(Res.string.title_text, viewModel.title, titleClicked)
            HorizontalDivider()
            InfoText(Res.string.description, viewModel.description, titleClicked)
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.pick_up),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )
            val pickUpLocation by viewModel.pickUpClickedLocation.collectAsState()
            HorizontalDivider()
            AddressText(pickUpLocation, pickUpLocationClicked)
            HorizontalDivider()
            DeliveryItemText(viewModel.pickUpDeliveryItem.value?.label, pickUpDeliveryClicked)
            HorizontalDivider()
            CarryText(viewModel.pickUpToggleState.value, pickUpDeliveryClicked)
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.drop_off),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )
            val dropOffLocation by viewModel.dropOffClickedLocation.collectAsState()
            HorizontalDivider()
            AddressText(dropOffLocation, dropOffLocationClicked)
            HorizontalDivider()
            DeliveryItemText(viewModel.dropOffDeliveryItem.value?.label, dropOffDeliveryClicked)
            HorizontalDivider()
            CarryText(viewModel.dropOffToggleState.value, dropOffDeliveryClicked)
            HorizontalDivider()
        }
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Turquoise),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(Res.string.publish_ad),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun InfoText(title: StringResource, info: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable {
            onClick.invoke()
        }.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(title), modifier = Modifier.padding(end = 24.dp))
        Text(info, modifier = Modifier.weight(1f), color = Color.Gray)
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Info",
            modifier = Modifier.size(28.dp),
            tint = Color.Gray
        )
    }
}

@Composable
private fun DeliveryItemText(text: StringResource?, deliveryClicked: () -> Unit) {
    text?.let {
        InfoText(Res.string.placement, stringResource(it), deliveryClicked)
    } ?: run {
        InfoText(Res.string.placement, "", deliveryClicked)
    }
}

@Composable
private fun AddressText(location: Pair<Double, Double>?, locationClicked: () -> Unit) {
    var address: String? by remember { mutableStateOf(null) }
    location?.let {
        LaunchedEffect(Unit) {
            address = getAddress(it.first, it.second)
        }
        InfoText(Res.string.address, address ?: "", locationClicked)
    }
}

@Composable
private fun CarryText(canHelp: Boolean, deliveryClicked: () -> Unit) {
    InfoText(
        Res.string.carry,
        if (canHelp) stringResource(Res.string.yes) else stringResource(Res.string.no),
        deliveryClicked
    )
}


package iq.tiptapp.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import tiptapp.composeapp.generated.resources.change_image
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
    imageCaptureClicked: () -> Unit,
    titleClicked: () -> Unit,
    pickUpLocationClicked: () -> Unit,
    pickUpDeliveryClicked: () -> Unit,
    dropOffLocationClicked: () -> Unit,
    dropOffDeliveryClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CustomTopAppBar(Res.string.publish_ad, onBackClicked)
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(state = scrollState)
                    .padding(bottom = 80.dp)
            ) {
                ImagesCarousel(viewModel.imageSlots, imageCaptureClicked)
                HorizontalDivider()
                InfoText(Res.string.title_text, viewModel.title, titleClicked)
                HorizontalDivider()
                InfoText(Res.string.description, viewModel.description, titleClicked)
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))
                TitleText(Res.string.pick_up)
                val pickUpLocation by viewModel.pickUpClickedLocation.collectAsState()
                HorizontalDivider()
                AddressText(pickUpLocation, pickUpLocationClicked)
                HorizontalDivider()
                DeliveryItemText(viewModel.pickUpDeliveryItem.value?.label, pickUpDeliveryClicked)
                HorizontalDivider()
                CarryText(viewModel.pickUpToggleState.value, pickUpDeliveryClicked)
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))
                TitleText(Res.string.drop_off)
                val dropOffLocation by viewModel.dropOffClickedLocation.collectAsState()
                HorizontalDivider()
                AddressText(dropOffLocation, dropOffLocationClicked)
                HorizontalDivider()
                DeliveryItemText(viewModel.dropOffDeliveryItem.value?.label, dropOffDeliveryClicked)
                HorizontalDivider()
                CarryText(viewModel.dropOffToggleState.value, dropOffDeliveryClicked)
                HorizontalDivider()
            }
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
private fun TitleText(title: StringResource) {
    Text(
        text = stringResource(title),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(12.dp)
    )
}

@Composable
private fun ImagesCarousel(
    imageSlots: SnapshotStateList<ByteArray?>,
    imageCaptureClicked: () -> Unit
) {
    if (imageSlots.filterNotNull().isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            items(imageSlots.size) { index ->
                val imageData = imageSlots[index]
                imageData?.let {
                    Image(
                        bitmap = it.decodeToImageBitmap(),
                        contentDescription = "Captured image",
                        modifier = Modifier
                            .width(120.dp)
                            .aspectRatio(3f / 4f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(Res.string.change_image),
        color = Color.Blue,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().clickable { imageCaptureClicked.invoke() },
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun InfoText(
    title: StringResource,
    info: String,
    onClick: () -> Unit
) {
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
private fun DeliveryItemText(
    textResource: StringResource?,
    deliveryClicked: () -> Unit
) {
    textResource?.let {
        InfoText(Res.string.placement, stringResource(it), deliveryClicked)
    } ?: run {
        InfoText(Res.string.placement, "", deliveryClicked)
    }
}

@Composable
private fun AddressText(
    location: Pair<Double, Double>?,
    locationClicked: () -> Unit
) {
    var address: String? by remember { mutableStateOf(null) }
    location?.let {
        LaunchedEffect(Unit) {
            address = getAddress(it.first, it.second)
        }
    }
    InfoText(Res.string.address, address ?: "", locationClicked)
}

@Composable
private fun CarryText(
    canHelp: Boolean,
    deliveryClicked: () -> Unit
) {
    InfoText(
        Res.string.carry,
        stringResource(if (canHelp) Res.string.yes else Res.string.no),
        deliveryClicked
    )
}


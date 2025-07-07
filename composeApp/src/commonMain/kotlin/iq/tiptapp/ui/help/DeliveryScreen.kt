package iq.tiptapp.ui.help

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import iq.tiptapp.Turquoise
import iq.tiptapp.component.CustomTopAppBar
import iq.tiptapp.domain.model.DeliveryNavItem
import iq.tiptapp.domain.model.Destination
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.continue_text
import tiptapp.composeapp.generated.resources.courtyard
import tiptapp.composeapp.generated.resources.driveway
import tiptapp.composeapp.generated.resources.garden
import tiptapp.composeapp.generated.resources.in_home
import tiptapp.composeapp.generated.resources.in_store
import tiptapp.composeapp.generated.resources.meet_show
import tiptapp.composeapp.generated.resources.outside
import tiptapp.composeapp.generated.resources.reception
import tiptapp.composeapp.generated.resources.stairwell

@Composable
fun DeliveryScreen(
    title: StringResource,
    onBackClicked: () -> Unit,
    onDetailScreen: () -> Unit,
    onNextScreen: () -> Unit
) {
    val navItems = remember {
        mutableStateListOf(
            DeliveryNavItem(Res.string.driveway, Destination.NextScreen),
            DeliveryNavItem(Res.string.stairwell, Destination.DetailScreen),
            DeliveryNavItem(Res.string.reception, Destination.NextScreen),
            DeliveryNavItem(Res.string.garden, Destination.NextScreen),
            DeliveryNavItem(Res.string.courtyard, Destination.DetailScreen),
            DeliveryNavItem(Res.string.outside, Destination.NextScreen),
            DeliveryNavItem(Res.string.meet_show, Destination.DetailScreen),
            DeliveryNavItem(Res.string.in_home, Destination.DetailScreen),
            DeliveryNavItem(Res.string.in_store, Destination.NextScreen)
        )
    }
    val selectedItem = remember { mutableStateOf<DeliveryNavItem?>(null) }

    Column(Modifier.fillMaxSize()) {
        CustomTopAppBar(title, onBackClicked)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            itemsIndexed(navItems) { index, item ->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navItems.forEachIndexed { i, oldItem ->
                                    navItems[i] = oldItem.copy(showCheckbox = i == index)
                                }
                                selectedItem.value = item
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(item.label),
                            modifier = Modifier.weight(1f)
                        )

                        if (item.showCheckbox) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "checked",
                                tint = Turquoise
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }

        Button(
            onClick = {
                when (selectedItem.value?.destination) {
                    is Destination.DetailScreen -> onDetailScreen.invoke()
                    is Destination.NextScreen -> onNextScreen.invoke()
                    null -> Napier.w("invalid destination")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Turquoise),
            enabled = selectedItem.value != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(Res.string.continue_text))
        }
    }
}
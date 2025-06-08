package iq.tiptapp.ui.help

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import component.CustomTopAppBar
import iq.tiptapp.Turquoise
import iq.tiptapp.map.MapComponent
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.continue_text
import tiptapp.composeapp.generated.resources.drop_off

@Composable
fun DropOffScreen(
    viewModel: HelpViewModel,
    onContinueClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            CustomTopAppBar(Res.string.drop_off, onBackClicked)
            MapComponent { lat, lng ->
                viewModel.onMarkerDropOffClicked(lat, lng)
            }
        }

        Button(
            onClick = {
                onContinueClicked.invoke()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Turquoise
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(stringResource(Res.string.continue_text))
        }
    }
}
package iq.tiptapp.map

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
import iq.tiptapp.component.CustomTopAppBar
import iq.tiptapp.Turquoise
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.continue_text

@Composable
fun MapScreen(
    title: StringResource,
    markerSnippet: StringResource,
    location: Pair<Double, Double>?,
    onContinueClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onMarkerClicked: (Double, Double) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            CustomTopAppBar(title, onBackClicked)
            MapComponent(location, stringResource(markerSnippet)) { lat, lng ->
                onMarkerClicked.invoke(lat, lng)
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
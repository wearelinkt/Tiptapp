package iq.tiptapp.delivery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import iq.tiptapp.Turquoise
import iq.tiptapp.component.CustomTopAppBar
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Door_code
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.continue_text
import tiptapp.composeapp.generated.resources.elevator
import tiptapp.composeapp.generated.resources.floor
import tiptapp.composeapp.generated.resources.info
import tiptapp.composeapp.generated.resources.other_info

@Composable
fun DeliveryDetailScreen(
    title: StringResource,
    onBackClicked: () -> Unit,
    toggleState: Boolean,
    onToggleState: (Boolean) -> Unit,
    floor: Int?,
    onFloorSelected: (Int) -> Unit,
    doorCode: Int?,
    onDoorCodeSelected: (Int) -> Unit,
    otherInfo: String?,
    onInfoEntered: (String) -> Unit,
    onContinueClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomTopAppBar(title, onBackClicked)
            LabelWithInput(label = Res.string.floor, hint = "1", floor, onFloorSelected)
            HorizontalDivider()
            LabelWithInput(
                label = Res.string.Door_code,
                hint = "12345",
                doorCode,
                onDoorCodeSelected
            )
            HorizontalDivider()
            ElevatorSwitch(toggleState, onToggleState)
            HorizontalDivider()
            MoreInfoRow(otherInfo, onInfoEntered)
            HorizontalDivider()
        }
        Button(
            onClick = onContinueClicked,
            colors = ButtonDefaults.buttonColors(containerColor = Turquoise),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(stringResource(Res.string.continue_text))
        }
    }
}

@Composable
fun LabelWithInput(
    label: StringResource,
    hint: String,
    myText: Int?,
    onItemSelected: (Int) -> Unit
) {
    var text by remember { mutableStateOf(myText?.toString() ?: "") }

    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(label),
            modifier = Modifier.width(96.dp)
        )
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                if (newText.all { it.isDigit() }) {
                    text = newText
                    onItemSelected.invoke(newText.toInt())
                }
            },
            cursorBrush = SolidColor(Turquoise),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(hint, color = Color.LightGray)
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun ElevatorSwitch(
    toggleState: Boolean,
    onToggleState: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(Res.string.elevator),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = toggleState,
            onCheckedChange = { onToggleState.invoke(it) },
            colors = SwitchDefaults.colors(
                checkedTrackColor = Turquoise
            )
        )
    }
}

@Composable
private fun MoreInfoRow(
    otherInfo: String?,
    onInfoEntered: (String) -> Unit
) {
    var text by remember { mutableStateOf(otherInfo ?: "") }
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.other_info),
            modifier = Modifier.width(96.dp)
        )
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onInfoEntered.invoke(it)
            },
            cursorBrush = SolidColor(Turquoise),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(stringResource(Res.string.info), color = Color.LightGray)
                }
                innerTextField()
            }
        )
    }
}
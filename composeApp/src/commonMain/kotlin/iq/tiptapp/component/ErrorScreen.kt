package component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.retry

@Composable
fun ErrorScreen(message: String, refresh: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, color = MaterialTheme.colors.onSurface, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Button(onClick = refresh) {
            Text(text = stringResource(Res.string.retry))
        }
    }
}
package iq.tiptapp.expected

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import io.github.aakira.napier.Napier

@Composable
actual fun SearchComponent(
    onPlaceSelected: (Double, Double) -> Unit,
    onDone: () -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val data = result.data
                val place = Autocomplete.getPlaceFromIntent(data!!)
                val latLng = place.latLng
                Napier.d("✅ Selected: ${place.name ?: "unknown"}")
                latLng?.let {
                    onPlaceSelected(latLng.latitude, latLng.longitude)
                }
            }

            AutocompleteActivity.RESULT_ERROR -> {
                val status: Status = Autocomplete.getStatusFromIntent(result.data!!)
                Napier.d("❌ Autocomplete error: ${status.statusMessage}")
                onDone()
            }

            Activity.RESULT_CANCELED -> {
                Napier.d("❌ Autocomplete cancelled")
                onDone()
            }
        }
    }

    // Launch autocomplete immediately
    LaunchedEffect(Unit) {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(context)
        launcher.launch(intent)
    }
}

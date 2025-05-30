package iq.tiptapp.ui.verification

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import component.TiptappAppBar
import iq.tiptapp.LightGray
import iq.tiptapp.Turquoise
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.continue_text
import tiptapp.composeapp.generated.resources.phone_number

@Composable
fun PhoneNumberInputScreen(
    viewModel: VerificationViewModel,
    onContinueClicked: () -> Unit
) {
    val context = LocalContext.current
    val phoneNumber = viewModel.phoneNumber
    val isLoading = viewModel.isLoading

    TiptappAppBar { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGray)
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = viewModel::onPhoneNumberChange,
                label = {
                    Text(stringResource(Res.string.phone_number))
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                leadingIcon = {
                    Text(
                        text = "+98",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Turquoise
                )
            )

            Button(
                onClick = {
                    viewModel.sendVerificationCode(
                        onSuccess = { onContinueClicked() },
                        onError = {
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Turquoise
                ),
                enabled = viewModel.isValidPhone(),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            ) {
                Text(stringResource(Res.string.continue_text))
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Turquoise)
                }
            }
        }
    }
}

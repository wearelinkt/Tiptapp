package iq.tiptapp

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import component.TiptappAppBar
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.continue_text
import tiptapp.composeapp.generated.resources.phone_number

@Composable
fun PhoneNumberInputScreen(
    phoneAuthService: PhoneAuthService,
    onContinueClicked: (String) -> Unit
) {
    val prefix = "+98"
    var phoneNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val fullPhoneNumber = prefix + phoneNumber
    val isValidPhone = phoneNumber.length in 9..10

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
                onValueChange = {
                    phoneNumber = it.filter(Char::isDigit).take(10)
                },
                label = {
                    Text(
                        stringResource(Res.string.phone_number)
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                // visualTransformation = PhoneNumberVisualTransformation(phoneNumberUtil),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                leadingIcon = {
                    Text(
                        text = prefix,
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
                    isLoading = true
                    phoneAuthService.sendVerificationCode(
                        fullPhoneNumber,
                        onCodeSent = {
                            isLoading = false
                            onContinueClicked.invoke(it)
                        },
                        onError = {
                            isLoading = false
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            println("Error sending code: ${it.message}")
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Turquoise
                ),
                enabled = isValidPhone,
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
                        .clickable(enabled = false) {}, // Prevent clicks behind the dialog
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Turquoise)
                }
            }
        }
    }
}
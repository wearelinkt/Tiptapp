package iq.tiptapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.TiptappAppBarWithNavigation
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.enter_code

@Composable
fun SmsCodeScreen(
    onBackClick: () -> Unit,
    onCodeComplete: (String, setLoading: (Boolean) -> Unit) -> Unit
) {
    val codeDigits = remember { List(6) { mutableStateOf("") } }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isLoading by remember { mutableStateOf(false) }

    TiptappAppBarWithNavigation(
        Res.string.enter_code, onBackClick, content =
        { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Turquoise)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    codeDigits.forEachIndexed { index, state ->
                        OutlinedTextField(
                            value = state.value,
                            onValueChange = { input ->
                                if (input.length <= 1 && (input.isEmpty() || input[0].isDigit())) {
                                    state.value = input

                                    // Move focus forward on input
                                    if (input.isNotEmpty() && index < 5) {
                                        focusRequesters[index + 1].requestFocus()
                                    }

                                    // Move focus back on delete
                                    if (input.isEmpty() && index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                    }

                                    // Check for complete code
                                    val fullCode = codeDigits.joinToString("") { it.value }
                                    if (fullCode.length == 6 && fullCode.all { it.isDigit() }) {
                                        keyboardController?.hide()
                                        onCodeComplete(fullCode) { loading ->
                                            isLoading = loading
                                        }
                                        isLoading = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(48.dp)
                                .height(64.dp)
                                .focusRequester(focusRequesters[index]),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Turquoise,
                                cursorColor = Turquoise
                            )
                        )
                    }
                }
            }
        }
    )
}
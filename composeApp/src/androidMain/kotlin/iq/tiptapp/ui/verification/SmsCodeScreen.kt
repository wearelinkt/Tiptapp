package iq.tiptapp.ui.verification

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import iq.tiptapp.component.TiptappAppBarWithNavigation
import iq.tiptapp.Turquoise
import iq.tiptapp.ui.splash.USER_ID_KEY
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.enter_code
import tiptapp.composeapp.generated.resources.user_register_failed

@Composable
fun SmsCodeScreen(
    prefs: DataStore<Preferences>,
    viewModel: VerificationViewModel,
    onBackClick: () -> Unit,
    navigate: () -> Unit
) {
    val isLoading = viewModel.isLoading
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequesters = remember { List(6) { FocusRequester() } }

    val codeDigits = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) {
        mutableStateListOf(*Array(6) { "" })
    }

    var focusedIndex by rememberSaveable { mutableIntStateOf(0) }

    val autoCode by viewModel.autoRetrievedCode.collectAsState()

    val focusManager = LocalFocusManager.current

    val registerUser by viewModel.registerUser.collectAsState()
    registerUser?.let {
        navigate.invoke()
        if(it) {
            LaunchedEffect(Unit) {
                prefs.edit { dataStore ->
                    dataStore[stringPreferencesKey(USER_ID_KEY)] =
                        viewModel.getUserId()
                }
            }
        } else {
            Toast.makeText(
                context,
                stringResource(Res.string.user_register_failed),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(autoCode) {
        if (autoCode.length == 6 && autoCode.all { it.isDigit() }) {
            focusManager.clearFocus()
            autoCode.forEachIndexed { i, c ->
                codeDigits[i] = c.toString()
            }
            keyboardController?.hide()
            viewModel.registerUser()
        } else {
            // Automatically focus first box on load
            focusRequesters[focusedIndex].requestFocus()
        }
    }

    TiptappAppBarWithNavigation(
        title = Res.string.enter_code,
        onBackClick = onBackClick
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Turquoise,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    codeDigits.forEachIndexed { index, value ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = { input ->
                                val digit =
                                    input.lastOrNull()?.takeIf { it.isDigit() }?.toString() ?: ""
                                codeDigits[index] = digit
                                focusedIndex = index

                                // Auto-move forward or back
                                if (digit.isNotEmpty() && index < 5) {
                                    focusRequesters[index + 1].requestFocus()
                                    focusedIndex = index + 1
                                } else if (digit.isEmpty() && index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                    focusedIndex = index - 1
                                }

                                // Combine and check for 6-digit code
                                val fullCode = codeDigits.joinToString("")
                                if (fullCode.length == 6 && fullCode.all { it.isDigit() }) {
                                    keyboardController?.hide()
                                    viewModel.updateSmsCode(fullCode)
                                    viewModel.verifyCode { success, result ->
                                        if (success) {
                                            viewModel.registerUser(result)
                                        } else {
                                            Toast.makeText(context, result, Toast.LENGTH_LONG)
                                                .show()
                                        }
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
    }
}
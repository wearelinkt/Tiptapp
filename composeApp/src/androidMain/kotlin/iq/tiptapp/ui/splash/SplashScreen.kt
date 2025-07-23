package iq.tiptapp.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import iq.tiptapp.Turquoise
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.check_network

@Composable
fun SplashScreen(
    prefs: DataStore<Preferences>,
    viewModel: SplashViewModel = koinViewModel<SplashViewModel>(),
    navigate: (Boolean) -> Unit
) {
    val userId by prefs
        .data
        .map {
            it[stringPreferencesKey(USER_ID_KEY)] ?: ""
        }
        .collectAsState(null)

    val userExist by viewModel.userExist.collectAsState()
    userExist?.let {
        navigate.invoke(it)
    }

    if (viewModel.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(color = Turquoise)
        }
    }

    if (viewModel.isConnected) {
        LaunchedEffect(userId) {
            userId?.let {
                if (it.isEmpty()) {
                    navigate.invoke(false)
                } else {
                    viewModel.userExist(it)
                }
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(stringResource(Res.string.check_network))
        }
    }

}

const val USER_ID_KEY = "user_id"
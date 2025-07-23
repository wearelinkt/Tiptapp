package iq.tiptapp.ui.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jordond.connectivity.Connectivity
import iq.tiptapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: UserRepository): ViewModel() {

    var isLoading by mutableStateOf(true)
        private set

    var isConnected by mutableStateOf(false)
        private set

    private val _userExist = MutableStateFlow<Boolean?>(null)
    val userExist: StateFlow<Boolean?>
        get() = _userExist

    private val connectivity = Connectivity()

    init {
        connectivity.start()
        viewModelScope.launch {
            connectivity.statusUpdates.collect { status ->
                when (status) {
                    is Connectivity.Status.Connected -> {
                        isConnected = true
                        isLoading = true
                    }
                    is Connectivity.Status.Disconnected -> {
                        isConnected = false
                        isLoading = false
                    }
                }
            }
        }
    }

    fun userExist(userId: String) {
        viewModelScope.launch {
            repository.userExist(userId).fold(
                onSuccess = {
                    _userExist.value = true
                    isLoading = false
                },
                onFailure = {
                    _userExist.value = false
                    isLoading = false
                }
            )
        }
    }
}
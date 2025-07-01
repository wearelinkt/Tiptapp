package iq.tiptapp.ui.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iq.tiptapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: UserRepository): ViewModel() {

    var isLoading by mutableStateOf(true)
        private set

    private val _userExist = MutableStateFlow<Boolean?>(null)
    val userExist: StateFlow<Boolean?>
        get() = _userExist

    fun userExist(userId: String) {
        isLoading = true
        viewModelScope.launch {
            repository.userExist(userId).fold(
                onSuccess = {
                    _userExist.value = true
                    hideLoading()
                },
                onFailure = {
                    _userExist.value = false
                    hideLoading()
                }
            )
        }
    }

    fun hideLoading() {
        isLoading = false
    }
}
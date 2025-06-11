package iq.tiptapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iq.tiptapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: UserRepository): ViewModel() {

    private val _userExist = MutableStateFlow<Boolean?>(null)
    val userExist: StateFlow<Boolean?>
        get() = _userExist

    fun userExist(userId: String) {
        viewModelScope.launch {
            repository.userExist(userId).fold(
                onSuccess = { result ->
                    _userExist.value = result
                },
                onFailure = {
                    _userExist.value = false
                }
            )
        }
    }
}
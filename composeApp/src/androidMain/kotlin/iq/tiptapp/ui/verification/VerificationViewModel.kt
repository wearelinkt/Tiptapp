package iq.tiptapp.ui.verification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iq.tiptapp.domain.repository.PhoneAuthService
import iq.tiptapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VerificationViewModel(
    private val phoneAuthService: PhoneAuthService,
    private val repository: UserRepository
) : ViewModel() {

    private val _autoRetrievedCode = MutableStateFlow("")
    val autoRetrievedCode: StateFlow<String> = _autoRetrievedCode

    var phoneNumber by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val _registerUserState = MutableStateFlow<Boolean?>(null)
    val registerUserState: StateFlow<Boolean?>
        get() = _registerUserState

    private var verificationId: String? = null
    private var smsCode: String = ""
    private var userId: String = "no-uid"

    private val fullPhoneNumber
        get() = "+98$phoneNumber"


    fun onPhoneNumberChange(newValue: String) {
        phoneNumber = newValue.filter(Char::isDigit).take(10)
    }

    fun updateSmsCode(newCode: String) {
        smsCode = newCode.take(6)
    }

    fun isValidPhone(): Boolean = phoneNumber.length in 9..10

    fun sendVerificationCode(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        isLoading = true
        phoneAuthService.sendVerificationCode(
            fullPhoneNumber,
            onCodeSent = { id ->
                isLoading = false
                verificationId = id
                onSuccess()
            },
            onAutoRetrievedCode = { code ->
                _autoRetrievedCode.value = code
                updateSmsCode(code)
            },
            onSuccess = {
                userId = it
                onSuccess()
            },
            onError = {
                isLoading = false
                onError(it)
            }
        )
    }

    fun verifyCode(
        onResult: (success: Boolean, userIdOrError: String) -> Unit
    ) {
        val verificationId = this.verificationId
        if (verificationId == null) {
            onResult(false, "Verification ID is missing")
            return
        }

        isLoading = true
        phoneAuthService.verifyCode(
            verificationId = verificationId,
            smsCode = smsCode,
            onSuccess = {
                onResult(true, it)
            },
            onError = {
                isLoading = false
                onResult(false, it.message ?: "Unknown error")
            }
        )
    }

    fun registerUser(userId: String? = null) {
        val id = userId ?: this.userId
        viewModelScope.launch {
            repository.registerUser(fullPhoneNumber, id).fold(
                onSuccess = {
                    isLoading = false
                    _registerUserState.value = true
                },
                onFailure = {
                    isLoading = false
                    _registerUserState.value = false
                }
            )
        }
    }

    fun getUserId() = userId
}
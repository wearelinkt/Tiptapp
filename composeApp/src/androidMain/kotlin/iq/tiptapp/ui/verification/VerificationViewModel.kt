package iq.tiptapp.ui.verification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import iq.tiptapp.PhoneAuthService

class VerificationViewModel(
    private val phoneAuthService: PhoneAuthService
) : ViewModel() {

    var phoneNumber by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    private var verificationId: String? = null
    private var smsCode: String = ""

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
        val fullPhoneNumber = "+98$phoneNumber"
        isLoading = true
        phoneAuthService.sendVerificationCode(
            fullPhoneNumber,
            onCodeSent = { id ->
                verificationId = id
                isLoading = false
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
                isLoading = false
                onResult(true, it)
            },
            onError = {
                isLoading = false
                onResult(false, it.message ?: "Unknown error")
            }
        )
    }
}


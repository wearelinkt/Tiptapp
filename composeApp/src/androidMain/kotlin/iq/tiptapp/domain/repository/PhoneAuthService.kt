package iq.tiptapp.domain.repository

interface PhoneAuthService {
    fun sendVerificationCode(
        phoneNumber: String,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoRetrievedCode: (code: String) -> Unit,
        onSuccess: (userId: String) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun verifyCode(
        verificationId: String,
        smsCode: String,
        onSuccess: (String) -> Unit, // return user ID or token
        onError: (Throwable) -> Unit
    )
}
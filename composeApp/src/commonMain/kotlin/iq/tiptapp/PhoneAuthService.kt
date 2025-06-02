package iq.tiptapp

interface PhoneAuthService {
    fun sendVerificationCode(
        phoneNumber: String,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoRetrievedCode: (code: String) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun verifyCode(
        verificationId: String,
        smsCode: String,
        onSuccess: (String) -> Unit, // return user ID or token
        onError: (Throwable) -> Unit
    )
}
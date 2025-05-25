package iq.tiptapp

interface PhoneAuthDelegate {
    fun sendCode(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun verifyCode(
        verificationId: String,
        smsCode: String,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    )
}
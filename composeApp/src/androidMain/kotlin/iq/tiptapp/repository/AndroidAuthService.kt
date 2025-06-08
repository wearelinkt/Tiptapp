package iq.tiptapp.repository

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import io.github.aakira.napier.Napier
import iq.tiptapp.domain.repository.PhoneAuthService
import java.util.concurrent.TimeUnit

class AndroidAuthService(
    private val activity: Activity,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : PhoneAuthService {

    override fun sendVerificationCode(
        phoneNumber: String,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoRetrievedCode: (code: String) -> Unit,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {

        /*
        val firebaseAuthSettings = auth.firebaseAuthSettings
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, "123456")
        */

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    val smsCode = credential.smsCode
                    if (!smsCode.isNullOrEmpty()) {
                        onAutoRetrievedCode(smsCode)
                    }
                    auth.signInWithCredential(credential)
                        .addOnSuccessListener {
                            onSuccess(it.user?.uid ?: "no-uid")
                        }
                        .addOnFailureListener { onError(it) }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onError(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Napier.d("code sent with verificationId :$verificationId")
                    onCodeSent(verificationId)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyCode(
        verificationId: String,
        smsCode: String,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { onSuccess(it.user?.uid ?: "no-uid") }
            .addOnFailureListener { onError(it) }
    }
}
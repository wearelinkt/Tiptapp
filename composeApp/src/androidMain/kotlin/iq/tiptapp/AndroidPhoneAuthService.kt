package iq.tiptapp

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AndroidPhoneAuthService(
    private val activity: Activity
) : PhoneAuthService {

    override fun sendVerificationCode(
        phoneNumber: String,
        onCodeSent: (verificationId: String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+13322767084")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity) // ✅ required
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnSuccessListener {
                            onCodeSent("auto-verification") // optionally return UID or notify user
                        }
                        .addOnFailureListener { onError(it) }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onError(e)
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
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
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnSuccessListener { onSuccess(it.user?.uid ?: "no-uid") }
            .addOnFailureListener { onError(it) }
    }
}

package com.naysayer.iseeclinic.login.phone

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class PhoneNumberSignInModel {

    private var mAuth = FirebaseAuth.getInstance()!!
    private val executor = DirectExecutor()
    private var mVerificationId: String? = null
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun startPhoneNumberVerification(phoneNumber: String,
                                     onSignInWithCredentialComplete: OnSignInWithCredentialComplete) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                executor,
                verificationStateChangedCallbacks(onSignInWithCredentialComplete))
    }

    private fun verificationStateChangedCallbacks(onSignInWithCredentialComplete: OnSignInWithCredentialComplete)
            : PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                signInWithPhoneAuthCredential(credential, onSignInWithCredentialComplete)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                if (e is FirebaseAuthInvalidCredentialsException) {
                    onSignInWithCredentialComplete.onError()
                } else if (e is FirebaseTooManyRequestsException) {
                    onSignInWithCredentialComplete.onError()
                }
            }

            override fun onCodeSent(verificationId: String?,
                                    token: PhoneAuthProvider.ForceResendingToken?) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,
                                              onSignInWithCredentialComplete: OnSignInWithCredentialComplete) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(executor, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        onSignInWithCredentialComplete.onComplete()
                    } else {
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            onSignInWithCredentialComplete.onCodeIsInvalid()
                        } else {
                            onSignInWithCredentialComplete.onError()
                        }
                    }
                })
    }

    fun signInWithCode(code: String, onSignInWithCredentialComplete: OnSignInWithCredentialComplete) {
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
        signInWithPhoneAuthCredential(credential, onSignInWithCredentialComplete)
    }
}

internal class DirectExecutor : Executor {
    override fun execute(r: Runnable) {
        r.run()
    }
}

interface OnSignInWithCredentialComplete {
    fun onComplete()
    fun onError()
    fun onCodeIsInvalid()
}
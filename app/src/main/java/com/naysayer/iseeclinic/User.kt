package com.naysayer.iseeclinic

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.naysayer.iseeclinic.login.UserActionResult
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class User {
    companion object Login {

        private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        private lateinit var mUserActionResult: UserActionResult
        private lateinit var mVerificationId: String
        private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken

        fun signInUser(email: String, password: String, userActionResult: UserActionResult) {
            mUserActionResult = userActionResult
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { successfulSignIn(it) }
        }

        fun signUpUser(name: String, email: String, password: String, userActionResult: UserActionResult) {
            mUserActionResult = userActionResult
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { successfulSignUp(it, name) }
        }

        fun signInUserAnonymously(userActionResult: UserActionResult) {
            mUserActionResult = userActionResult
            mAuth.signInAnonymously()
                    .addOnCompleteListener { successfulSignInAnonymously(it) }
        }

        fun sendPasswordResetEmail(email: String, userActionResult: UserActionResult) {
            mUserActionResult = userActionResult
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { successfulEmailSending(it) }
        }

        fun confirmationOfPhoneNumber(phoneNumber: String, userActionResult: UserActionResult) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    DirectExecutor(),
                    verificationStateChangedCallbacks(userActionResult))
        }

        fun signInWithCredential(credential: AuthCredential, userActionResult: UserActionResult) {
            mUserActionResult = userActionResult
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener { resultOfSignInWithCredential(it) }

        }

        fun signInWithPhoneAuthCredential(code: String, userActionResult: UserActionResult) {
            val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(DirectExecutor(), OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            userActionResult.successfullyAuth()
                        } else {
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                userActionResult.unsuccessfullyAuth(task.exception!!)
                            } else {
                                userActionResult.unsuccessfullyAuth()
                            }
                        }
                    })
        }

        private fun successfulSignUp(task: Task<AuthResult>, name: String) {
            when (task.isSuccessful) {
                true -> {
                    mUserActionResult.successfullyAuth()
                    updateUserName(name)
                }
                false -> {
                    mUserActionResult.unsuccessfullyAuth()
                }
            }
        }

        private fun successfulSignIn(task: Task<AuthResult>) {
            when (task.isSuccessful) {
                true -> {
                    mUserActionResult.successfullyAuth()
                }
                false -> {
                    mUserActionResult.unsuccessfullyAuth()
                }
            }
        }

        private fun successfulSignInAnonymously(task: Task<AuthResult>) {
            when (task.isSuccessful) {
                true -> {
                    mUserActionResult.successfullyAuth()
                }
                false -> {
                    mUserActionResult.unsuccessfullyAuth()
                }
            }
        }

        private fun successfulEmailSending(task: Task<Void>) {
            when (task.isSuccessful) {
                true -> {
                    mUserActionResult.successfullyAuth()
                }
                false -> {
                    mUserActionResult.unsuccessfullyAuth()
                }
            }
        }

        private fun resultOfSignInWithCredential(task: Task<AuthResult>) {
            if (task.isSuccessful) {
                mUserActionResult.successfullyAuth()
            } else {
                mUserActionResult.unsuccessfullyAuth()
            }
        }

        private fun updateUserName(userName: String) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()
            mAuth.currentUser!!.updateProfile(profileUpdates)
        }

        private fun verificationStateChangedCallbacks(userActionResult: UserActionResult)
                : PhoneAuthProvider.OnVerificationStateChangedCallbacks {
            return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    signInWithCredential(credential, userActionResult)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        userActionResult.unsuccessfullyAuth(e)
                    } else if (e is FirebaseTooManyRequestsException) {
                        userActionResult.unsuccessfullyAuth(e)
                    }
                }

                override fun onCodeSent(verificationId: String?,
                                        token: PhoneAuthProvider.ForceResendingToken?) {
                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId!!
                    mResendToken = token!!
                }
            }
        }
    }
}

internal class DirectExecutor : Executor {
    override fun execute(r: Runnable) {
        r.run()
    }
}
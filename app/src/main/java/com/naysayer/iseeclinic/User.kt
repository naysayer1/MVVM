package com.naysayer.iseeclinic

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.naysayer.iseeclinic.login.UserActionResult

class User {
    companion object Login {

        private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        private lateinit var mUserActionResult: UserActionResult

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

        fun signInWithCredential(credential: AuthCredential, userActionResult: UserActionResult) {
            mUserActionResult = userActionResult
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener { resultOfSignInWithCredential(it) }

        }

        private fun successfulSignUp(task: Task<AuthResult>, name: String) {
            when (task.isSuccessful) {
                true -> {
                    mUserActionResult.successfully()
                    updateUserName(name)
                }
                false -> {
                    mUserActionResult.unsuccessfully()
                }
            }
        }

        private fun successfulSignIn(task: Task<AuthResult>) {
            when (task.isSuccessful) {
                true -> {
                    mUserActionResult.successfully()
                }
                false -> {
                    mUserActionResult.unsuccessfully()
                }
            }
        }

        private fun successfulSignInAnonymously(task: Task<AuthResult>) {
            when (task.isSuccessful) {
                true -> {
                    mUserActionResult.successfully()
                }
                false -> {
                    mUserActionResult.unsuccessfully()
                }
            }
        }

        private fun successfulEmailSending(task: Task<Void>) {
            when (task.isSuccessful) {
                true -> {
                    mUserActionResult.successfully()
                }
                false -> {
                    mUserActionResult.unsuccessfully()
                }
            }
        }

        private fun resultOfSignInWithCredential(task: Task<AuthResult>) {
            if (task.isSuccessful) {
                mUserActionResult.successfully()
            } else {
                mUserActionResult.unsuccessfully()
            }
        }

        private fun updateUserName(userName: String) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()
            mAuth.currentUser!!.updateProfile(profileUpdates)
        }
    }
}
package com.naysayer.iseeclinic.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.naysayer.iseeclinic.Validation


class LoginModel {

    private var mAuth = FirebaseAuth.getInstance()!!
    private val validation = Validation()

    fun isEmailValid(email: String) = validation.isEmailValid(email)

    fun isPasswordValid(password: String) = validation.isPasswordValid(password)

    fun isUserAlreadyExist(): Boolean {
        if (mAuth.currentUser != null) {
            return true
        }
        return false
    }

    fun signIn(email: String, password: String, authResults: AuthResults) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { successfulLogin(it, authResults) }
    }


    fun signInAnonymously(authResults: AuthResults) {
        mAuth.signInAnonymously()
                .addOnCompleteListener { successfulLogin(it, authResults) }
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, authResults: AuthResults) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (!it.isSuccessful) {
                //todo обработка ошибки
            } else {
                authResults.successfully()
            }
        }
    }

    fun signUp(name: String, email: String, password: String, authResults: AuthResults) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { successfulRegistration(name, it, authResults) }
    }

    fun sendPasswordResetEmail(email: String, authResults: AuthResults) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { successful(it, authResults) }
    }

    private fun successfulLogin(task: Task<AuthResult>,
                                authResults: AuthResults) {
        if (task.isSuccessful) {
            authResults.successfully()
        } else {
            authResults.unsuccessfully()
        }
    }

    private fun successfulRegistration(name: String,
                                       task: Task<AuthResult>,
                                       authResults: AuthResults) {
        if (task.isSuccessful) {
            val user = mAuth.currentUser
            if (user != null) {
                updateUserName(name, user)
                authResults.successfully()
            }
        } else {
            when (task.exception) {
                is FirebaseAuthUserCollisionException -> authResults.unsuccessfully()
            }
        }
    }

    private fun updateUserName(name: String, user: FirebaseUser) {
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        user.updateProfile(profileUpdates)

    }

    private fun successful(task: Task<Void>, authResults: AuthResults) {
        if (!task.isSuccessful) {
            when (task.exception) {
                is FirebaseAuthEmailException -> {
                    authResults.unsuccessfully()
                }
            }
        } else {
            authResults.successfully()
        }
    }
}

interface AuthResults {
    fun successfully()
    fun unsuccessfully()
}

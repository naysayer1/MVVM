package com.naysayer.iseeclinic.login.usual

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.naysayer.iseeclinic.User
import com.naysayer.iseeclinic.util.Validation
import com.naysayer.iseeclinic.login.UserActionResult


class LoginModel {

    private var mAuth = FirebaseAuth.getInstance()
    private val validation = Validation()

    fun isEmailValid(email: String) = validation.isEmailValid(email)

    fun isPasswordValid(password: String) = validation.isPasswordValid(password)

    fun isUserAlreadyExist(): Boolean {
        return mAuth.currentUser != null
    }

    fun signIn(email: String, password: String, userActionResult: UserActionResult) {
        User.signInUser(email, password, userActionResult)
    }

    fun signUp(name: String, email: String, password: String, userActionResult: UserActionResult) {
        User.signUpUser(name, email, password, userActionResult)
    }

    fun signInAnonymously(userActionResult: UserActionResult) {
        User.signInUserAnonymously(userActionResult)
    }

    fun sendPasswordResetEmail(email: String, userActionResult: UserActionResult) {
        User.sendPasswordResetEmail(email, userActionResult)
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, userActionResult: UserActionResult) {
        User.signInWithCredential(GoogleAuthProvider.getCredential(acct.idToken, null), userActionResult)
    }
}

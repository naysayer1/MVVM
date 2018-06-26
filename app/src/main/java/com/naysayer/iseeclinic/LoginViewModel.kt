package com.naysayer.iseeclinic

import android.arch.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var user = User()
    var email = ""
    var password = ""

    fun signIn() {
        user.signIn(email, password)
    }

    fun signUp() {
        user.signUp(email, password)
    }

    fun googleSignIn() {
        user.googleSignIn()
    }

    fun forgotPassword() {
        user.forgotPassword()
    }
}
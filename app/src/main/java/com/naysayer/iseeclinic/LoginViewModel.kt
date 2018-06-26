package com.naysayer.iseeclinic

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var loginModel = LoginModel(getApplication())
    var email = ""
    var password = ""

    fun signIn() {
        loginModel.signIn(email, password)
    }

    fun signUp() {
        loginModel.signUp(email, password)
    }

    fun googleSignIn() {
        loginModel.googleSignIn()
    }

    fun forgotPassword() {
        loginModel.forgotPassword()
    }
}
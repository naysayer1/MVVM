package com.naysayer.iseeclinic.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var loginModel = LoginModel(getApplication())
    var email = ""
    var password = ""
    var isLoading = ObservableField<Boolean>(false)

    fun signIn() {
        isLoading.set(true)
       // loginModel.signIn(email, password)
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
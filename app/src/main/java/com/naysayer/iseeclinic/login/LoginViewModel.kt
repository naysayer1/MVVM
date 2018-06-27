package com.naysayer.iseeclinic.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var loginModel = LoginModel(getApplication())
    var email = ""
    var password = ""
    var isLoading = ObservableField<Boolean>()

    private val userAuth = object : UserAuth {
        override fun successAuth() {
            isLoading.set(false)
        }
    }

    fun signIn() {
        isLoading.set(true)
        loginModel.signIn(userAuth, email, password)
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

    fun checkEmailValidation(text: CharSequence) {
        //Todo проверять емаил и показывать ошибку, если не правильно указан
    }

    fun checkPasswordValidation(text: CharSequence) {
        //Todo проверять пароль и показывать ошибку, если не правильно указан
    }
}
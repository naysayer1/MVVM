package com.naysayer.iseeclinic.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.naysayer.iseeclinic.Event


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var loginModel = LoginModel(getApplication())
    private val _showResetPasswordDialog = MutableLiveData<Event<Boolean>>()
    private val _showEmailIsNotValidError = MutableLiveData<Event<Boolean>>()
    private val _showPasswordIsNotValidError = MutableLiveData<Event<Boolean>>()

    val showResetPasswordDialog: LiveData<Event<Boolean>>
        get() = _showResetPasswordDialog

    val showEmailIsNotValidError: LiveData<Event<Boolean>>
        get() = _showEmailIsNotValidError

    val showPasswordIsNotValidError: LiveData<Event<Boolean>>
        get() = _showPasswordIsNotValidError

    var email = ""  //TODO возможно не нужно
    var password = ""
    var isLoading = ObservableField<Boolean>()

    fun signIn() {
        isLoading.set(true)
        loginModel.signIn(email, password)
    }

    fun signUp() {
        loginModel.signUp(email, password)
    }

    fun googleSignIn() {
        loginModel.googleSignIn()
    }

    fun forgotPassword() {
        _showResetPasswordDialog.value = Event(true)
    }

    fun checkEmailValidation(text: CharSequence) {
        val isValid = loginModel.isEmailValid(text.toString())
        _showEmailIsNotValidError.value = Event(content = isValid)

    }

    fun checkPasswordValidation(text: CharSequence) {
        val isValid = loginModel.isPasswordValid(text.toString())
        _showPasswordIsNotValidError.value = Event(content = isValid)
    }
}
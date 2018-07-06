package com.naysayer.iseeclinic.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.util.Log
import com.naysayer.iseeclinic.Event


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var loginModel = LoginModel(getApplication())
    private val _showResetPasswordDialog = MutableLiveData<Event<Boolean>>()
    private val _showEmailIsNotValidError = MutableLiveData<Event<Boolean>>()
    private val _showPasswordIsNotValidError = MutableLiveData<Event<Boolean>>()
    private val _showResetPasswordEmailSend = MutableLiveData<Event<Boolean>>()
    private var isEmailValid = false
    private var isPasswordValid = false
    var email = ""
    var password = ""
    var isLoading = ObservableField<Boolean>()

    val showResetPasswordDialog: LiveData<Event<Boolean>>
        get() = _showResetPasswordDialog

    val showEmailIsNotValidError: LiveData<Event<Boolean>>
        get() = _showEmailIsNotValidError

    val showPasswordIsNotValidError: LiveData<Event<Boolean>>
        get() = _showPasswordIsNotValidError

    val showResetPasswordEmailSend: LiveData<Event<Boolean>>
        get() = _showResetPasswordEmailSend

    fun signIn() {
        if (!isEmailValid or !isPasswordValid) {
            //TODO емаил или пароль неверны при входе
            Log.d("Error", "Invalid email or password")
        } else {
            isLoading.set(true)
            loginModel.signIn(email, password)
        }
    }

    fun signUp() {
        if (!isEmailValid or !isPasswordValid) {
            //TODO емаил или пароль неверны при регистрации
            Log.d("Error", "Invalid email or password")
        } else {
            isLoading.set(true)
            loginModel.signUp(email, password)
        }
    }

    fun googleSignIn() {
        loginModel.googleSignIn()
    }

    fun forgotPassword() {
        _showResetPasswordDialog.value = Event(true)
    }

    fun sendPasswordResetEmail(email: String) {
        //TODO надо возвращять положительный или отрицательный результат из viewmodel
        loginModel.sendPasswordResetEmail(email)
        _showResetPasswordEmailSend.value = Event(content = true)
    }

    fun checkEmailValidation(text: CharSequence) {
        isEmailValid = loginModel.isEmailValid(text.toString())
        _showEmailIsNotValidError.value = Event(content = isEmailValid)
        email = text.toString()
    }

    fun checkPasswordValidation(text: CharSequence) {
        isPasswordValid = loginModel.isPasswordValid(text.toString())
        _showPasswordIsNotValidError.value = Event(content = isPasswordValid)
        password = text.toString()
    }
}
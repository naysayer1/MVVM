package com.naysayer.iseeclinic.login.usual

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.naysayer.iseeclinic.util.Event
import com.naysayer.iseeclinic.login.UserActionResult


class LoginViewModel : ViewModel() {

    private var loginModel = LoginModel()

    private var isEmailValid = false
    private var isPasswordValid = false
    private var email = ""
    private var password = ""
    private var name = ""

    private val _showResetPasswordDialog = MutableLiveData<Event<Boolean>>()
    private val _showEmailIsNotValidError = MutableLiveData<Event<Boolean>>()
    private val _showPasswordIsNotValidError = MutableLiveData<Event<Boolean>>()
    private val _showEmailCollisionErrorToast = MutableLiveData<Event<Boolean>>()
    private val _showInvalidEmailOrPasswordToast = MutableLiveData<Event<Boolean>>()
    private val _showPasswordResetEmailSendingResultToast = MutableLiveData<Event<Boolean>>()
    private val _showSignUpForm = MutableLiveData<Event<Boolean>>()
    private val _startMainActivity = MutableLiveData<Event<Boolean>>()
    private val _startGoogleSignInActivity = MutableLiveData<Event<Boolean>>()
    private val _startSignInWithPhoneNumberActivity = MutableLiveData<Event<Boolean>>()

    val showResetPasswordDialog: LiveData<Event<Boolean>>
        get() = _showResetPasswordDialog

    val showEmailIsNotValidError: LiveData<Event<Boolean>>
        get() = _showEmailIsNotValidError

    val showPasswordIsNotValidError: LiveData<Event<Boolean>>
        get() = _showPasswordIsNotValidError

    val showEmailCollisionErrorToast: LiveData<Event<Boolean>>
        get() = _showEmailCollisionErrorToast

    val showInvalidEmailOrPasswordToast: LiveData<Event<Boolean>>
        get() = _showInvalidEmailOrPasswordToast

    val showPasswordResetEmailSendingResultToast: LiveData<Event<Boolean>>
        get() = _showPasswordResetEmailSendingResultToast

    val showSignUpForm: LiveData<Event<Boolean>>
        get() = _showSignUpForm

    val startMainActivity: LiveData<Event<Boolean>>
        get() = _startMainActivity

    val startGoogleSignInActivity: LiveData<Event<Boolean>>
        get() = _startGoogleSignInActivity

    val startSignInWithPhoneNumberActivity: LiveData<Event<Boolean>>
        get() = _startSignInWithPhoneNumberActivity

    val isLoading = ObservableField<Boolean>()
    val isSignUp = ObservableField<Boolean>(false)

    fun isUserAlreadyExist() {
        if (loginModel.isUserAlreadyExist()) {
            _startMainActivity.value = Event(true)
        }
    }

    private fun signInUser() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            isLoading.set(true)
            loginModel.signIn(email, password, signInResult())
        }
    }

    private fun signUpUser() {
        if (isEmailValid && isPasswordValid) {
            isLoading.set(true)
            loginModel.signUp(name, email, password, signUpResult())
        }
    }

    fun signInOrSignUp() {
        if (isSignUp.get()!!) {
            signUpUser()
        } else {
            signInUser()
        }
    }

    fun signInWithGoogle() {
        _startGoogleSignInActivity.value = Event(true)
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        isLoading.set(true)
        loginModel.firebaseAuthWithGoogle(acct, signInWithGoogleResult())
    }

    fun signInAnonymously() {
        isLoading.set(true)
        loginModel.signInAnonymously(signInAnonymouslyResult())
    }

    fun signInWithPhoneNumber() {
        _startSignInWithPhoneNumberActivity.value = Event(true)
    }

    fun forgotPassword() {
        _showResetPasswordDialog.value = Event(true)
    }

    fun sendPasswordResetEmail(email: String) {
        loginModel.sendPasswordResetEmail(email, emailSendingResult())
    }

    fun switchLoginMethod() {
        _showSignUpForm.value = Event(true)
        isSignUp.set(!isSignUp.get()!!)

        // Разрешаем показывать ошибки при вводе эл. адреса или пароля
        _showPasswordIsNotValidError.value = Event(true)
        _showEmailIsNotValidError.value = Event(true)
    }

    fun checkEmailValidation(text: CharSequence) {
        if (isSignUp.get()!!) {
            isEmailValid = loginModel.isEmailValid(text.toString())
            _showEmailIsNotValidError.value = Event(isEmailValid)
        }
        email = text.toString()
    }

    fun checkPasswordValidation(text: CharSequence) {
        if (isSignUp.get()!!) {
            isPasswordValid = loginModel.isPasswordValid(text.toString())
            _showPasswordIsNotValidError.value = Event(isPasswordValid)
        }
        password = text.toString()
    }

    fun getUserName(text: CharSequence) {
        name = text.toString()
    }

    private fun signInWithGoogleResult(): UserActionResult {
        return object : UserActionResult {
            override fun successfully() {
                _startMainActivity.value = Event(true)
                isLoading.set(false)
            }

            override fun unsuccessfully() {
                //TODO обработка ошибок входа с гуглом
            }

        }
    }

    private fun signInResult(): UserActionResult {
        return object : UserActionResult {
            override fun successfully() {
                _startMainActivity.value = Event(true)
                isLoading.set(false)
            }

            override fun unsuccessfully() {
                _showInvalidEmailOrPasswordToast.value = Event(true)
                isLoading.set(false)
            }
        }
    }

    private fun signUpResult(): UserActionResult {
        return object : UserActionResult {
            override fun successfully() {
                _startMainActivity.value = Event(true)
                isLoading.set(false)
            }

            override fun unsuccessfully() {
                _showEmailCollisionErrorToast.value = Event(true)
                isLoading.set(false)
            }
        }
    }

    private fun signInAnonymouslyResult(): UserActionResult {
        return object : UserActionResult {
            override fun successfully() {
                _startMainActivity.value = Event(true)
                isLoading.set(false)
            }

            override fun unsuccessfully() {
                //TODO: Обработать ошибки анонимного входа
                isLoading.set(false)
            }
        }
    }

    private fun emailSendingResult(): UserActionResult {
        return object : UserActionResult {
            override fun successfully() {
                _showPasswordResetEmailSendingResultToast.value = Event(true)
            }

            override fun unsuccessfully() {
                _showPasswordResetEmailSendingResultToast.value = Event(false)
            }
        }
    }
}
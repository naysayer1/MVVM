package com.naysayer.iseeclinic.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.naysayer.iseeclinic.Event
import com.naysayer.iseeclinic.SingleLiveData


class LoginViewModel : ViewModel() {

    private var loginModel = LoginModel()

    private val _showResetPasswordDialog = MutableLiveData<Event<Boolean>>()
    private val _showEmailIsNotValidError = MutableLiveData<Event<Boolean>>()
    private val _showPasswordIsNotValidError = MutableLiveData<Event<Boolean>>()

    private var isEmailValid = false
    private var isPasswordValid = false
    val isLoading = ObservableField<Boolean>()
    val isSignUp = ObservableField<Boolean>(false)
    private var email = ""
    private var password = ""
    private var name = ""

    val showResetPasswordDialog: LiveData<Event<Boolean>>
        get() = _showResetPasswordDialog

    val showEmailIsNotValidError: LiveData<Event<Boolean>>
        get() = _showEmailIsNotValidError

    val showPasswordIsNotValidError: LiveData<Event<Boolean>>
        get() = _showPasswordIsNotValidError

    val startMainActivityEvent = SingleLiveData<Pair<LoginModel, Boolean>>()
    val startGoogleSignInActivityEvent = SingleLiveData<Pair<LoginModel, Boolean>>()
    val showSnackbar = SingleLiveData<Pair<LoginModel, Boolean>>()
    val showSignUp = SingleLiveData<Pair<LoginModel, Boolean?>>()
    val showEmailCollisionToast = SingleLiveData<Pair<LoginModel, Boolean>>()
    val showInvalidEmailOrPasswordToast = SingleLiveData<Pair<LoginModel, Boolean>>()

    fun isUserAlreadyExist() {
        if (loginModel.isUserAlreadyExist()) {
            startMainActivityEvent.value = loginModel to true
        }
    }

    private fun signInUser() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            isLoading.set(true)
            loginModel.signIn(email, password, loginResult())
        }
    }

    private fun signUpUser() {
        if (isEmailValid && isPasswordValid) {
            isLoading.set(true)
            loginModel.signUp(name, email, password, registrationResult())
        }
    }

    fun signInOrSignUp() {
        when (isSignUp.get()) {
            true -> {
                // Start sign up user
                signUpUser()
            }
            false -> {
                //Start sign in user
                signInUser()
            }
        }
    }

    fun signInWithGoogle() {
        startGoogleSignInActivityEvent.value = loginModel to true
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        isLoading.set(true)
        loginModel.firebaseAuthWithGoogle(acct, loginResult())
    }

    fun signInAnonymously() {
        isLoading.set(true)
        loginModel.signInAnonymously(loginResult())
    }

    fun signInWithPhoneNumber(){
        //TODO Sign in with phone number
    }

    fun forgotPassword() {
        _showResetPasswordDialog.value = Event(true)
    }

    fun switchLoginMethod() {
        showSignUp.value = loginModel to isSignUp.get()
        isSignUp.set(!isSignUp.get()!!)
        _showPasswordIsNotValidError.value = Event(true)
        _showEmailIsNotValidError.value = Event(true)
    }

    fun sendPasswordResetEmail(email: String) {
        loginModel.sendPasswordResetEmail(email, sendingEmailResult())
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

    private fun registrationResult(): AuthResults {
        return object : AuthResults {
            override fun successfully() {
                startMainActivityEvent.value = loginModel to true
                isLoading.set(false)
            }

            override fun unsuccessfully() {
                showEmailCollisionToast.value = loginModel to true
                isLoading.set(false)
            }

        }
    }

    private fun loginResult(): AuthResults {
        return object : AuthResults {
            override fun successfully() {
                startMainActivityEvent.value = loginModel to true
                isLoading.set(false)
            }

            override fun unsuccessfully() {
                showInvalidEmailOrPasswordToast.value = loginModel to true
                isLoading.set(false)
                //TODO обработка ошибок входа(обычного, с гуглом, анонимного)
            }

        }
    }

    private fun sendingEmailResult(): AuthResults {
        return object : AuthResults {
            override fun successfully() {
                showSnackbar.value = loginModel to true
            }

            override fun unsuccessfully() {
                showSnackbar.value = loginModel to false
            }
        }
    }
}
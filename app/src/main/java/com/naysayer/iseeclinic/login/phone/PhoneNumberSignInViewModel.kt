package com.naysayer.iseeclinic.login.phone

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.naysayer.iseeclinic.util.Event

class PhoneNumberSignInViewModel {

    private val viewModel = PhoneNumberSignInModel()

    val isLoading = ObservableField<Boolean>()

    private val _startRegistrationOfPhoneNumber = MediatorLiveData<Event<Boolean>>()
    val startRegistrationOfPhoneNumber: LiveData<Event<Boolean>>
        get() = _startRegistrationOfPhoneNumber

    private val _startMainActivityIfCodeIsValid = MutableLiveData<Event<Boolean>>()
    val startMainActivityIfCodeIsValid: LiveData<Event<Boolean>>
        get() = _startMainActivityIfCodeIsValid

    fun registerNumber() {
        _startRegistrationOfPhoneNumber.value = Event(true)
    }

    fun startPhoneNumberVerification(phoneNumber: String,
                                     onSignInWithCredentialComplete: OnSignInWithCredentialComplete) {
        viewModel.startPhoneNumberVerification(phoneNumber, onSignInWithCredentialComplete)
    }

    fun checkCode() {
        _startMainActivityIfCodeIsValid.value = Event(true)
    }

    fun signInWithCode(code: String,
                       onSignInWithCredentialComplete: OnSignInWithCredentialComplete) {
        isLoading.set(true)
        viewModel.signInWithCode(code, onSignInWithCredentialComplete)
    }

}
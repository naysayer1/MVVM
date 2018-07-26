package com.naysayer.iseeclinic.login.phone

import com.naysayer.iseeclinic.User
import com.naysayer.iseeclinic.login.UserActionResult

class PhoneNumberSignInModel {

    fun startPhoneNumberVerification(phoneNumber: String, userActionResult: UserActionResult) {
        User.confirmationOfPhoneNumber(phoneNumber, userActionResult)
    }

    fun signInWithCode(code: String, userActionResult: UserActionResult) {
        User.signInWithPhoneAuthCredential(code, userActionResult)
    }
}

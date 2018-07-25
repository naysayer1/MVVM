package com.naysayer.iseeclinic.login.phone

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.naysayer.iseeclinic.R
import com.naysayer.iseeclinic.databinding.ActivityPhoneNumberSignInBinding
import com.naysayer.iseeclinic.main.MainActivity
import com.naysayer.iseeclinic.toast
import com.transitionseverywhere.Slide
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet
import kotlinx.android.synthetic.main.activity_phone_number_sign_in.*


class PhoneNumberSignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneNumberSignInBinding
    private lateinit var phoneNumberSignInViewModel: PhoneNumberSignInViewModel

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PhoneNumberSignInActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        phoneNumberSignInViewModel = PhoneNumberSignInViewModel()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_number_sign_in)
        binding.viewModel = phoneNumberSignInViewModel
        binding.executePendingBindings()

        phoneNumberSignInViewModel
                .startRegistrationOfPhoneNumber
                .observe(this, Observer {
                    hideKeyboard()
                    val phoneNumber = getPhoneNumber()

                    when {
                        phoneNumber.length in 11..14 -> {
                            phoneNumberSignInViewModel.startPhoneNumberVerification(phoneNumber, phoneVerificationResultsCallbacks())
                            showCardForCodeEntering(phoneNumber)
                        }
                        else -> {
                            phoneNumberAppCompatEditText.hint = getString(R.string.phone_number_sign_in_hint)
                            toast(this, getString(R.string.phone_number_sign_in_incorrect_number))
                        }
                    }
                })

        phoneNumberSignInViewModel
                .startMainActivityIfCodeIsValid
                .observe(this, Observer {
                    val code = edit_text_code.text.toString()
                    if (code.isNotEmpty()) {
                        phoneNumberSignInViewModel.signInWithCode(code, phoneVerificationResultsCallbacks())
                    }
                })
    }

    private fun getPhoneNumber(): String {
        countryCodePicker.registerPhoneNumberTextView(phoneNumberAppCompatEditText)
        return countryCodePicker.fullNumberWithPlus
    }

    private fun showCardForCodeEntering(phoneNumber: String) {
        setTransitions()

        card_view_entering_phone.visibility = View.GONE
        card_view_entering_code.visibility = View.VISIBLE

        text_view_phone_number_sign_in_number.text = phoneNumber
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }

    private fun setTransitions() {
        TransitionManager.beginDelayedTransition(rootlayout_phone_sign_in,
                TransitionSet().addTransition(Slide(Gravity.END)
                        .addTarget(card_view_entering_code))
                        .addTransition(Slide(Gravity.START)
                                .addTarget(card_view_entering_phone))
                        .setInterpolator(LinearOutSlowInInterpolator())
                        .setDuration(600))
    }

    private fun startMainActivity() {
        startActivity(MainActivity.newIntent(this@PhoneNumberSignInActivity))
        finish()
    }

    private fun phoneVerificationResultsCallbacks(): OnSignInWithCredentialComplete {
        return object : OnSignInWithCredentialComplete {
            override fun onComplete() {
                phoneNumberSignInViewModel.isLoading.set(false)
                startMainActivity()
            }

            override fun onError() {
                toast(this@PhoneNumberSignInActivity,
                        getString(R.string.phone_number_sign_in_error))
            }

            override fun onCodeIsInvalid() {
                toast(this@PhoneNumberSignInActivity,
                        getString(R.string.phone_number_sign_in_incorrect_code))
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

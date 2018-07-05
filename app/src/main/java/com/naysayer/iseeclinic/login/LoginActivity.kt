package com.naysayer.iseeclinic.login

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.naysayer.iseeclinic.Dialogs
import com.naysayer.iseeclinic.OnDialogButtonsClick
import com.naysayer.iseeclinic.R
import com.naysayer.iseeclinic.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dialogs = Dialogs(this)

        loginViewModel = LoginViewModel(application)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = loginViewModel
        binding.executePendingBindings()

        loginViewModel.showResetPasswordDialog.observe(this, Observer {
            dialogs.resetPasswordDialog(object : OnDialogButtonsClick {
                override fun onPositiveClick() {
                    Toast.makeText(this@LoginActivity, "Yes", Toast.LENGTH_SHORT).show()
                }
            })
        })

        loginViewModel.showEmailIsNotValidError.observe(this, Observer {
            when {
                !it!!.peekContent() -> {
                    textinputlayout_login_email.error = getString(R.string.error_email_is_not_valid)
                }
                else -> textinputlayout_login_email.error = ""
            }
        })

        loginViewModel.showPasswordIsNotValidError.observe(this, Observer {
            when {
                !it!!.peekContent() -> {
                    textinputlayout_login_password.error = getString(R.string.error_password_is_not_valid)
                }
                else -> textinputlayout_login_password.error = ""
            }
        })
    }
}
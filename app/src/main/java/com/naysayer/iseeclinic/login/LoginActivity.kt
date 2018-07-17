package com.naysayer.iseeclinic.login

import android.arch.lifecycle.Observer
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.naysayer.iseeclinic.Dialogs
import com.naysayer.iseeclinic.OnDialogButtonsClick
import com.naysayer.iseeclinic.R
import com.naysayer.iseeclinic.R.id.button_login_sign_in
import com.naysayer.iseeclinic.R.id.card_view_login
import com.naysayer.iseeclinic.SIGN_IN_WITH_GOOGLE
import com.naysayer.iseeclinic.databinding.ActivityLoginBinding
import com.naysayer.iseeclinic.main.MainActivity
import com.transitionseverywhere.AutoTransition
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dialogs = Dialogs(this)

        loginViewModel = LoginViewModel()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = loginViewModel
        binding.executePendingBindings()

        loginViewModel.isUserAlreadyExist()

        loginViewModel.showResetPasswordDialog.observe(this, Observer {
            dialogs.resetPasswordDialog(object : OnDialogButtonsClick {
                override fun onInput(c: CharSequence) {
                    loginViewModel.sendPasswordResetEmail(c.toString())
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

        loginViewModel.showSnackbar.observe(this, Observer {
            if (it!!.second) {
                Snackbar.make(findViewById(R.id.rootlayout_login), R.string.reset_password_email_content, Snackbar.LENGTH_LONG)
                        .setAction(R.string.reset_password_email_button) {}.show()
            } else {
                Toast.makeText(this, "Не удалось отправить емаил", Toast.LENGTH_LONG).show()
            }
        })

        loginViewModel.showSignUp.observe(this, Observer {
            setTransitions()
        })

        loginViewModel.showInvalidEmailOrPasswordToast.observe(this, Observer {
            Toast.makeText(this, "Вы неправильно указали эл. адрес или пароль", Toast.LENGTH_SHORT).show()
        })

        loginViewModel.showEmailCollisionToast.observe(this, Observer {
            Toast.makeText(this, "Пользователь с таким емайлом уже существует", Toast.LENGTH_SHORT).show()
        })

        loginViewModel.startMainActivityEvent.observe(this, Observer {
            if (it!!.second) {
                startMainActivity()
            }
        })

        loginViewModel.startGoogleSignInActivityEvent.observe(this, Observer {
            if (it!!.second) {
                startGoogleSignInActivity()
            }
        })
    }

    private fun setTransitions() {
        TransitionManager.beginDelayedTransition(card_view_login,
                TransitionSet()
                        .addTransition(ChangeText()
                                .setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN)
                                .addTarget(button_login_sign_in)).setDuration(250)
                        .addTransition(AutoTransition()).setDuration(250))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SIGN_IN_WITH_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                loginViewModel.firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.newIntent(this))
        finish()
    }

    private fun startGoogleSignInActivity() {
        val signInIntent = getGoogleSignInClient()?.signInIntent
        startActivityForResult(signInIntent, SIGN_IN_WITH_GOOGLE)
    }

    private fun getGoogleSignInClient(): GoogleSignInClient? {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))
                .requestEmail()
                .build()

        return GoogleSignIn.getClient(this, googleSignInOptions)
    }
}


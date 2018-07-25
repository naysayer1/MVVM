package com.naysayer.iseeclinic.login.usual

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.naysayer.iseeclinic.Dialogs
import com.naysayer.iseeclinic.OnDialogButtonsClick
import com.naysayer.iseeclinic.R
import com.naysayer.iseeclinic.databinding.ActivityLoginBinding
import com.naysayer.iseeclinic.login.phone.PhoneNumberSignInActivity
import com.naysayer.iseeclinic.main.MainActivity
import com.naysayer.iseeclinic.toast
import com.transitionseverywhere.AutoTransition
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private val mDialogs = Dialogs(this)

    companion object {
        private const val SIGN_IN_WITH_GOOGLE = 1009

        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = LoginViewModel()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = loginViewModel
        binding.executePendingBindings()

        loginViewModel.isUserAlreadyExist()

        startObserving()
    }

    private fun startObserving() {
        // Диалог о сбросе пароля
        loginViewModel.showResetPasswordDialog.observe(this, Observer {
            mDialogs.resetPasswordDialog(object : OnDialogButtonsClick {
                override fun onInput(c: CharSequence) {
                    loginViewModel.sendPasswordResetEmail(c.toString())
                }
            })
        })

        // Показывает ошибки при вводе эл. адреса (только когда пользователь регестрируется)
        loginViewModel.showEmailIsNotValidError.observe(this, Observer {
            when (it!!.peekContent()) {
                true -> {
                    textinputlayout_login_email.error = ""
                }
                false -> {
                    textinputlayout_login_email.error = getString(R.string.error_email_is_not_valid)
                }
            }
        })

        // Показывает ошибки при вводе пароля (только когда пользователь регестрируется)
        loginViewModel.showPasswordIsNotValidError.observe(this, Observer {
            when (it!!.peekContent()) {
                true -> {
                    textinputlayout_login_email.error = ""
                }
                false -> {
                    textinputlayout_login_email.error = getString(R.string.error_password_is_not_valid)
                }
            }
        })

        // Сообщение о успешной/неуспешной доставке письма
        loginViewModel.showPasswordResetEmailSendingResultToast.observe(this, Observer {
            if (it!!.peekContent()) {
                toast(this, getString(R.string.reset_password_email_content))
            } else {
                toast(this, getString(R.string.sending_email_error))
            }
        })

        // Изменияем форму регистрации
        loginViewModel.showSignUpForm.observe(this, Observer {
            setTransitions()
        })

        // Сообщение о том, что при попытке входа был указан неверный эл. адрес или пароль
        loginViewModel.showInvalidEmailOrPasswordToast.observe(this, Observer {
            toast(this, getString(R.string.incorrect_email_or_password))
        })

        // Сообщение о том, что такой эл. адресс уже занят
        loginViewModel.showEmailCollisionErrorToast.observe(this, Observer {
            toast(this, getString(R.string.email_collision))
        })

        // Запуск MainActivity
        loginViewModel.startMainActivity.observe(this, Observer {
            startMainActivity()
        })

        // Запуск GoogleActivity
        loginViewModel.startGoogleSignInActivity.observe(this, Observer {
            startGoogleSignInActivity()
        })

        // Запуск PhoneNumberSignInActivity
        loginViewModel.startSignInWithPhoneNumberActivity.observe(this, Observer {
            startPhoneNumberSignInActivity()
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

    private fun startPhoneNumberSignInActivity() {
        startActivity(PhoneNumberSignInActivity.newIntent(this))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun getGoogleSignInClient(): GoogleSignInClient? {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))
                .requestEmail()
                .build()

        return GoogleSignIn.getClient(this, googleSignInOptions)
    }
}


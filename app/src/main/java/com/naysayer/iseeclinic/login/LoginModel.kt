package com.naysayer.iseeclinic.login

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.naysayer.iseeclinic.Validation
import com.naysayer.iseeclinic.main.MainActivity

class LoginModel(private var context: Context) {

    private var mAuth = FirebaseAuth.getInstance()!!
    private lateinit var mUser: FirebaseUser
    private val validation = Validation()
    var b = false

    fun isEmailValid(email: String): Boolean {
        return validation.isEmailValid(email)
    }

    fun isPasswordValid(password: String): Boolean {
        return validation.isPasswordValid(password)
    }

    fun ifUserAlreadyExist() {
        if (mAuth.currentUser != null) {
            startMainActivity()
        }
    }

    fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task -> successfulAuth(task) }


        //TODO("Пользователь успешно создается -> (нужен ли тут отдельный поток) -> отправить пользователя в аккаунт Так же нужно обработать возможные ошибки при создание пользователя")
    }

    fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task -> successfulAuth(task) }

    }

    fun googleSignIn() {

    }

    fun sendPasswordResetEmail(email: String) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task: Task<Void> -> b = task.isSuccessful }
    }

    private fun successfulAuth(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            mUser = mAuth.currentUser!!
            startMainActivity()
        } else {
            when (task.exception) {
                is FirebaseAuthUserCollisionException -> {
                    //TODO Пользователь с таким email'ом существует
                }
            }
        }
    }

    private fun successful(task: Task<Void>) {
        if (!task.isSuccessful) {
            when (task.exception) {
                is FirebaseAuthEmailException -> {
                    //TODO обработка ошибки
                }
            }
        } else {

        }
    }

    private fun startMainActivity() {
        val intent = MainActivity.newIntent(context)
        context.startActivity(intent)
    }
}

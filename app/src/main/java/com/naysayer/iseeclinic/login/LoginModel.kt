package com.naysayer.iseeclinic.login

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.naysayer.iseeclinic.main.MainActivity

class LoginModel(private var context: Context) {

    private var mAuth = FirebaseAuth.getInstance()!!
    private lateinit var mUser: FirebaseUser

    fun signUp(email: String, password: String ) {
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

    fun forgotPassword() {
        mAuth.sendPasswordResetEmail(mUser.email!!)
        //Todo Диалог, подтверждающий, что полшьзователь действительно хочет сбросить пароль
    }

    private fun successfulAuth(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            mUser = mAuth.currentUser!!
            startMainActivity()
        } else {

        }
        //TODO запускать основное активити
    }

    private fun startMainActivity() {
        val intent = MainActivity.newIntent(context)
        context.startActivity(intent)
    }
}

interface UserAuth {
    fun successAuth()
}

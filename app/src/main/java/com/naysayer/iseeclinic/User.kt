package com.naysayer.iseeclinic

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class User() {

    private var mAuth = FirebaseAuth.getInstance()!!
    lateinit var mUser: FirebaseUser

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

    fun forgotPassword() {
        mAuth.sendPasswordResetEmail(mUser.email!!)
        //Todo Диалог, подтверждающий, что полшьзователь действительно хочет сбросить пароль
    }

    private fun successfulAuth(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            mUser = mAuth.currentUser!!
        } else {

        }
        //TODO запускать основное активити
    }
}

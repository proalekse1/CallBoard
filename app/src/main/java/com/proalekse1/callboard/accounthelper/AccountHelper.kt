package com.proalekse1.callboard.accounthelper

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.proalekse1.callboard.MainActivity
import com.proalekse1.callboard.R

class AccountHelper(act: MainActivity) { //класс для регистрации
    private val act = act

    fun signUpWithEmail(email:String, password:String){
        if(email.isNotEmpty() && password.isNotEmpty()){ //проверяем что не пустые почта и пароль
            act.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task-> //создать user
                if(task.isSuccessful){ //проверяем удалось отправить почту и пароль
                    //если успешно зарегистрировались запустится условие отправить письмо с подтверждением
                    sendEmailVerification(task.result?.user!!) //!! может быть null, но мы берем на себя ответственность
                    act.uiUpdate(task.result?.user)
                } else{
                    Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun signInWithEmail(email:String, password:String){ //функция для входа
        if(email.isNotEmpty() && password.isNotEmpty()){ //проверяем что не пустые почта и пароль
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task-> //вход с пользователем и паролем
                if(task.isSuccessful){ //проверяем удалось отправить почту и пароль
                    //если успешно зарегистрировались запустится условие отправить письмо с подтверждением
                    act.uiUpdate(task.result?.user) //показывает аккаунт в хидере
                } else{
                    Toast.makeText(act, act.resources.getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser){ //функция для отправки подтверждающего письма
        user.sendEmailVerification().addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(act, act.resources.getString(R.string.send_verification_done), Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(act, act.resources.getString(R.string.send_verification_error), Toast.LENGTH_LONG).show()
            }
        }
    }
}
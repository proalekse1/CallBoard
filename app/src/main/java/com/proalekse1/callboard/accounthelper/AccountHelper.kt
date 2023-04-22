package com.proalekse1.callboard.accounthelper

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.proalekse1.callboard.MainActivity
import com.proalekse1.callboard.R
import com.proalekse1.callboard.dialoghelper.GoogleAccConst

class AccountHelper(act: MainActivity) { //класс для регистрации
    private val act = act
    private lateinit var signInClient: GoogleSignInClient //переменная для хранения данных с гугл аккаунта

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

    private fun getSignInClient(): GoogleSignInClient { //функция для регистрации по гугл аккаунту, отправляет запрос смартфону чтобы получить доступ к гугл аккаунту на телефоне
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(act.getString(R.string.default_web_client_id)).build() //запрос в гугл аккаунт на телефоне
        return GoogleSignIn.getClient(act,gso) // возвращаем полученные данные из запроса
    }

    fun signInWithGoogle(){ //для передачи на майн активити для входа в гугл по нажатию кнопки
        signInClient = getSignInClient() //присвоили переменной полученные данные гугл аккаунта
        val intent = signInClient.signInIntent //передаем намерение на вход с гугл аккаунтом
        act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signInFirebaseWithGoogle(token:String){ //для регистрации с гугл аккаунт на Firebase
        val credential = GoogleAuthProvider.getCredential(token, null) //получить учетные данные по токену
        act.mAuth.signInWithCredential(credential).addOnCompleteListener{task-> // регистрируемся
            if (task.isSuccessful){
                Toast.makeText(act, "Sign in done", Toast.LENGTH_LONG).show() //если успешно зарегистрировались запустим эту функцию
            }
        } // регистрируемся
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
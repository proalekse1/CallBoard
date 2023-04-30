package com.proalekse1.callboard.accounthelper

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.proalekse1.callboard.MainActivity
import com.proalekse1.callboard.R
import com.proalekse1.callboard.constans.FirebaseAuthConstans
import com.proalekse1.callboard.dialoghelper.GoogleAccConst

class AccountHelper(act: MainActivity) { //класс для регистрации
    private val act = act
    private lateinit var signInClient: GoogleSignInClient //переменная для хранения данных с гугл аккаунта

    fun signUpWithEmail(email: String, password: String) { //регистрация по почте
        if (email.isNotEmpty() && password.isNotEmpty()) { //проверяем что не пустые почта и пароль
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task -> //создать user
                    if (task.isSuccessful) { //проверяем удалось отправить почту и пароль
                        //если успешно зарегистрировались запустится условие отправить письмо с подтверждением
                        sendEmailVerification(task.result?.user!!) //!! может быть null, но мы берем на себя ответственность
                        act.uiUpdate(task.result?.user) //показываем потовый адрес в хидере
                    } else {
                        //Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                         Log.d("MyLog", "Exception : " + task.exception) //ловим класс ошибки
                        if (task.exception is FirebaseAuthUserCollisionException) { //ловим ошибку если аккаунт уже существует с такой почтой
                            val exception = task.exception as FirebaseAuthUserCollisionException //Делаем каст превращая таск в эту ошибку
                            // Log.d("MyLog", "Exception : ${exception.errorCode}") //получаем код ошибки в логкат
                            if (exception.errorCode == FirebaseAuthConstans.ERROR_EMAIL_ALREADY_IN_USE) { //если аккаунт уже сущствует
                                // Toast.makeText(act, FirebaseAuthConstans.ERROR_EMAIL_ALREADY_IN_USE, Toast.LENGTH_LONG).show()
                                // связываем аккаунты почты и гугл аккаунта 10 урок
                                linkEmailToG(email, password) //связываем аккаунты
                            }

                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) { //ловим ошибку если неверно ввели почту поставили пробел или собаку
                            val exception = task.exception as FirebaseAuthInvalidCredentialsException //Делаем каст превращая таск в эту ошибку
                            // Log.d("MyLog", "Exception : ${exception.errorCode}") //получаем код ошибки в логкат
                            if (exception.errorCode == FirebaseAuthConstans.ERROR_INVALID_EMAIL) { //если неверно ввелеи акккаунт
                                Toast.makeText(act, FirebaseAuthConstans.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                            }
                        }

                        if (task.exception is FirebaseAuthWeakPasswordException) { //ловим ошибку если неверно ввели пароль
                            val exception = task.exception as FirebaseAuthWeakPasswordException //Делаем каст превращая таск в эту ошибку
                             Log.d("MyLog", "Exception : ${exception.errorCode}") //получаем код ошибки в логкат
                            if (exception.errorCode == FirebaseAuthConstans.ERROR_WEAK_PASSWORD) { //если неверно ввели пароль - мало символов
                                Toast.makeText(act, FirebaseAuthConstans.ERROR_WEAK_PASSWORD, Toast.LENGTH_LONG).show()
                            }
                        }


                    }
                }
        }
    }

    private fun linkEmailToG(email: String, password: String){ //для объединения аккаунтов получаем почту и пароль
        val credential = EmailAuthProvider.getCredential(email,password) //получили данные
        if (act.mAuth.currentUser != null){ //если пользоваель не нал то сязываем
        act.mAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->  //объединили аккаунты и проверили все ли удалось
            if (task.isSuccessful) {
                Toast.makeText(act, act.resources.getString(R.string.link_done), Toast.LENGTH_LONG)
                    .show()
            }
        }
        } else {
            Toast.makeText(act, act.resources.getString(R.string.enter_to_G), Toast.LENGTH_LONG).show()
        }
    }

    private fun getSignInClient(): GoogleSignInClient { //функция для регистрации по гугл аккаунту, отправляет запрос смартфону чтобы получить доступ к гугл аккаунту на телефоне
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(act.getString(R.string.default_web_client_id)).requestEmail().build() //запрос в гугл аккаунт на телефоне
        return GoogleSignIn.getClient(act,gso) // возвращаем полученные данные из запроса
    }

    fun signInWithGoogle(){ //для передачи на майн активити для входа в гугл по нажатию кнопки
        signInClient = getSignInClient() //присвоили переменной полученные данные гугл аккаунта
        val intent = signInClient.signInIntent //передаем намерение на вход с гугл аккаунтом
        act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signOutG(){ //для выхода из гугл аккаунта
        getSignInClient().signOut()
    }

    fun signInFirebaseWithGoogle(token:String){ //для регистрации с гугл аккаунт на Firebase
        val credential = GoogleAuthProvider.getCredential(token, null) //получить учетные данные по токену
        act.mAuth.signInWithCredential(credential).addOnCompleteListener{task-> // регистрируемся
            if (task.isSuccessful){
                Toast.makeText(act, "Sign in done", Toast.LENGTH_LONG).show() //если успешно зарегистрировались запустим эту функцию
                act.uiUpdate(task.result?.user)
            } else {
                Log.d("MyLog", "Google Sign In Exception : ${task.exception}") //ловим номер ошибки
            }
        } // регистрируемся
    }

    fun signInWithEmail(email:String, password:String){ //функция для входа по почте
        if(email.isNotEmpty() && password.isNotEmpty()){ //проверяем что не пустые почта и пароль
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task-> //вход с пользователем и паролем
                if(task.isSuccessful){ //проверяем удалось отправить почту и пароль
                    //если успешно зарегистрировались запустится условие отправить письмо с подтверждением
                    act.uiUpdate(task.result?.user) //показывает аккаунт в хидере
                } else{
                    Log.d("MyLog", "Exception : ${task.exception}") //получаем класс ошибки в логкат
                    if (task.exception is FirebaseAuthInvalidCredentialsException) { //ловим ошибку если неверно ввели почту поставили пробел или собаку

                        val exception = task.exception as FirebaseAuthInvalidCredentialsException //Делаем каст превращая таск в эту ошибку
                         Log.d("MyLog", "Exception : ${exception.errorCode}") //получаем код ошибки в логкат
                        if (exception.errorCode == FirebaseAuthConstans.ERROR_INVALID_EMAIL) { //если неверно ввели акккаунт
                            Toast.makeText(act, FirebaseAuthConstans.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                        } else if (exception.errorCode == FirebaseAuthConstans.ERROR_WRONG_PASSWORD) { //если аккаунт уже сущствует
                            Toast.makeText(act, FirebaseAuthConstans.ERROR_WRONG_PASSWORD, Toast.LENGTH_LONG).show()
                        }
                    } else if(task.exception is FirebaseAuthInvalidUserException) { //если такого юзера не сущесвует
                        val exception = task.exception as FirebaseAuthInvalidUserException //Делаем каст превращая таск в эту ошибку
                        Log.d("MyLog", "Exception : ${exception.errorCode}") //получаем класс ошибки в логкат
                        if (exception.errorCode == FirebaseAuthConstans.ERROR_USER_NOT_FOUND) { //если неверно ввели акккаунт
                            Toast.makeText(act, FirebaseAuthConstans.ERROR_USER_NOT_FOUND, Toast.LENGTH_LONG).show()
                        }
                    }
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
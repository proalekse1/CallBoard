package com.proalekse1.callboard.dialoghelper

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.proalekse1.callboard.MainActivity
import com.proalekse1.callboard.R
import com.proalekse1.callboard.accounthelper.AccountHelper
import com.proalekse1.callboard.databinding.SignDialogBinding


class DialogHelper(act:MainActivity) { //диалог для регистрации и входа
    private val act = act //act это контекст
    private val accHelper = AccountHelper(act) //инициализируем функцию регистрации

    fun createSignDialog(index:Int){ //создаем диалог
        val builder = AlertDialog.Builder(act) //инициализировали билдер
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater) //подключили байндинг к разметке и надули
        val view = rootDialogElement.root
        builder.setView(view)//передаем разметку в билдер

        setDialogState(index, rootDialogElement) //функция выбора индекса входа или регистрации

        val dialog = builder.create() //создали диалог
        rootDialogElement.btSignUpIn.setOnClickListener{//слушатель нажатий
            setOnClickSignUpIn(index, rootDialogElement, dialog) //слушатель нажатий в диалоге
        }
        rootDialogElement.btForgetP.setOnClickListener{//слушатель нажатий
            setOnClickResetPassword(rootDialogElement, dialog) //слушатель нажатий в диалоге
        }

        dialog.show() //рисуем диалог на экране

    }

    private fun setOnClickResetPassword(rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {

        if(rootDialogElement.edSignEmail.text.isNotEmpty()){ //если поле email не пустое
            act.mAuth.sendPasswordResetEmail(rootDialogElement.edSignEmail.text.toString()).addOnCompleteListener{task->//отправка ссылки для восстановления пароля
                if(task.isSuccessful){
                    Toast.makeText(act, R.string.email_reset_password_was_sent, Toast.LENGTH_LONG).show()
                }
            }
            dialog?.dismiss() //закроет диалог после нажатия
        } else {
            rootDialogElement.tvDialogMessage.visibility = View.VISIBLE //делаем видимой подсказку если не введена почта
        }

    }

    private fun setOnClickSignUpIn(index: Int, rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        dialog?.dismiss() //закрыть диалог
        if(index == DialogConst.SIGN_UP_STATE){

            accHelper.signUpWithEmail(rootDialogElement.edSignEmail.text.toString(), //запускаем функцию регистрации
                rootDialogElement.edSignPassword.text.toString())

        } else {
            accHelper.signInWithEmail(rootDialogElement.edSignEmail.text.toString(), //запускаем функцию входа
                rootDialogElement.edSignPassword.text.toString())
        }
    }

    private fun setDialogState(index: Int, rootDialogElement: SignDialogBinding) {
        if(index == DialogConst.SIGN_UP_STATE){ //проверяем что нажали вход или регистрация
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_up) //показываем текст через байндинг в диалоге
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
        } else {
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_in)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
            rootDialogElement.btForgetP.visibility = View.VISIBLE //видимая кнопка для восстанов пароля
        }
    }
}
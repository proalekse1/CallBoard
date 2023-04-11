package com.proalekse1.callboard.dialoghelper

import android.app.AlertDialog
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
        if(index == DialogConst.SIGN_UP_STATE){ //проверяем что нажали вход или регистрация
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_up) //показываем текст через байндинг в диалоге
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
        } else {
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_in)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
        }

        rootDialogElement.btSignUpIn.setOnClickListener{//слушатель нажатий
            if(index == DialogConst.SIGN_UP_STATE){
                accHelper.signUpWithEmail(rootDialogElement.edSignEmail.text.toString(), //запускаем функцию регистрации
                    rootDialogElement.edSignPassword.text.toString())
            } else {

            }

        }
        builder.setView(view)//передаем разметку в билдер
        builder.show() //рисуем диалог на экране
    }
}
package com.proalekse1.callboard.dialoghelper

import android.app.Activity
import android.app.AlertDialog
import com.proalekse1.callboard.databinding.ProgressDialogLayoutBinding
import com.proalekse1.callboard.databinding.SignDialogBinding

object ProgressDialog { //прогресс бар

    fun createProgressDialog(act: Activity): AlertDialog{ //создаем диалог

        val builder = AlertDialog.Builder(act) //инициализировали билдер
        val rootDialogElement = ProgressDialogLayoutBinding.inflate(act.layoutInflater) //подключили байндинг к разметке и надули
        val view = rootDialogElement.root
        builder.setView(view)//передаем разметку в билдер
        val dialog = builder.create()
        dialog.setCancelable(false) //блокируем кнопки пока идет прогресс
        dialog.show() //рисуем диалог на экране
        return dialog

    }

}
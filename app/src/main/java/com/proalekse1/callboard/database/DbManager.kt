package com.proalekse1.callboard.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager { //для управления базой данных из Ad.kt

    val db = Firebase.database.getReference("main") //подключили fire base к переменной

    fun publishAd(){ //функция будет записывать в базу данных данные

        db.setValue("Hello")
    }
}
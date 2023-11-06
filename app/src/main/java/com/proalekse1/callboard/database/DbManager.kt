package com.proalekse1.callboard.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.proalekse1.callboard.data.Ad

class DbManager { //для управления базой данных из Ad.kt

    val db = Firebase.database.getReference("main") //подключили fire base к переменной
    val auth = Firebase.auth

    fun publishAd(ad: Ad){ //функция будет записывать в базу данных данные

       if (auth.uid != null)db.child(ad.key ?: "empty").child(auth.uid!!).child("ad").setValue(ad) //если UID не null то запишем в базу данных объявление
    }
}
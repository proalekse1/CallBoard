package com.proalekse1.callboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DbManager { //для управления базой данных из Ad.kt

    val db = Firebase.database.getReference("main") //подключили fire base к переменной
    val auth = Firebase.auth

    fun publishAd(ad: Ad){ //функция будет записывать в базу данных данные

       if (auth.uid != null)db.child(ad.key ?: "empty").child(auth.uid!!).child("ad").setValue(ad) //если UID не null то запишем в базу данных объявление
    }

    fun readDataFromDb(readDataCallback: ReadDataCallback?){ //функция считыания данных
        db.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) { //снимок базы выдает файл snapshot
                val adArray = ArrayList<Ad>() //создаем список
                for(item in snapshot.children) { //snapshot.children путь к ключу
                    val ad = item.children.iterator().next().child("ad").getValue(Ad::class.java) //полный путь до узла ad записанный в переменную
                    if (ad != null) adArray.add(ad) //помещаем в список данные из объявления
                }
                readDataCallback?.readData(adArray)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    interface ReadDataCallback { //интерфейс для передачи данных полученных из базы данных в ресайклер вью
        fun readData(list: ArrayList<Ad>)
    }
}
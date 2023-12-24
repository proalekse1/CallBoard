package com.proalekse1.callboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DbManager { //для управления базой данных из Ad.kt

    val db = Firebase.database.getReference("main") //подключили fire base к переменной для считывания с главного пути
    val auth = Firebase.auth

    fun publishAd(ad: Ad, finishListener: FinishWorkListener){ //функция будет записывать в базу данных данные

       if (auth.uid != null)db.child(ad.key ?: "empty").child(auth.uid!!).child("ad") //если UID не null то запишем в базу данных объявление
           .setValue(ad).addOnCompleteListener {
               if(it.isSuccessful)
               finishListener.onFinish()
           }
    }

    fun getMyAds(readDataCallback: ReadDataCallback){ //функция фильтрации по уид
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid) //запрос по пути где получаем уид объявления
        readDataFromDb(query, readDataCallback)

    }
    fun getAllAds(readDataCallback: ReadDataCallback){ //запрос всех объявлений
        val query = db.orderByChild(auth.uid + "/ad/price") //
        readDataFromDb(query, readDataCallback)
    }

    fun deleteAd(ad: Ad, listener: FinishWorkListener){ //для удаления объявлений из базы данных
        if(ad.key == null || ad.uid == null) return
        db.child(ad.key).child(ad.uid).removeValue().addOnCompleteListener {
            if(it.isSuccessful) listener.onFinish()
        }
    }

   private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?){ //функция считыания данных
        query.addListenerForSingleValueEvent(object: ValueEventListener {

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

    interface FinishWorkListener{ //интерфейс для прослушиания успешной передачи данных в базу данных
        fun onFinish()
    }
}
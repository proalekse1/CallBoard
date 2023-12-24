package com.proalekse1.callboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proalekse1.callboard.model.Ad
import com.proalekse1.callboard.model.DbManager

class FirebaseViewModel: ViewModel() { //посредник между базой данных и View из архитектуры MVVM

    private val dbManager = DbManager() //инициализируем класс
    val liveAdsData = MutableLiveData<ArrayList<Ad>>() //следит за тем когда надо обновить View
    fun loadAllAds(){ //функция передачи данных из bdManager в liveAdsData
        dbManager.getAllAds(object: DbManager.ReadDataCallback{ //доступ к интерфейсу
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list                        //получили список с объявлениями
            }
        })
    }

    fun loadMyAds(){ //функция передачи данных из bdManager в liveAdsData
        dbManager.getMyAds(object: DbManager.ReadDataCallback{ //доступ к интерфейсу
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list                        //получили список с объявлениями
            }
        })
    }

    fun deleteItem(ad: Ad){ //посредник для удаления объявлений
        dbManager.deleteAd(ad, object: DbManager.FinishWorkListener{
            override fun onFinish() {
                val updatedList = liveAdsData.value
                updatedList?.remove(ad)
                liveAdsData.postValue(updatedList)
            }
        })
    }
}
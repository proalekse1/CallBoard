package com.proalekse1.callboard.database

import com.proalekse1.callboard.data.Ad

interface ReadDataCallback { //интерфейс для передачи данных полученных из базы данных в ресайклер вью
    fun readData(list: List<Ad>)
}
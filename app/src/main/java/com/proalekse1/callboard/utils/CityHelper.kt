package com.proalekse1.callboard.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object CityHelper { //получение городов и стран из json файла
    fun getAllCountries(context: Context):ArrayList<String>{
        var tempArray = ArrayList<String>()
        try{

            val inputStream : InputStream = context.assets.open("countriesToCities.json") //достаем через контекст джейсон файл
            val size:Int = inputStream.available() //выясняем размер массива с городами и странами
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray) //считываем массив
            val jsonFile = String(bytesArray) //превращаем в стринг
            val jsonObject = JSONObject(jsonFile) //превращаем в джейсон объект
            val countriesNames = jsonObject.names() //получаем все имена стран
            if(countriesNames != null) {
                for (n in 0 until countriesNames.length()) { //прогоняем массив с именами стран
                    tempArray.add(countriesNames.getString(n)) //и записываем в наш массив
                }
            }
        } catch (e:IOException){

        }
        return tempArray
    }
}
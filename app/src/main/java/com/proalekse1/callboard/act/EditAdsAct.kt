package com.proalekse1.callboard.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.proalekse1.callboard.R
import com.proalekse1.callboard.databinding.ActivityEditAdsBinding
import com.proalekse1.callboard.dialogs.DialogSpinnerHelper
import com.proalekse1.callboard.utils.CityHelper

class EditAdsAct : AppCompatActivity() { //активити для новых объявлений
    lateinit var rootElement:ActivityEditAdsBinding //для байндинга
    private val dialog = DialogSpinnerHelper() //инициализируем диалог

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater) //для байндинга
        setContentView(rootElement.root) //для байндинга
        init()
    }

    private fun init(){

    }

    //OnClicks
    fun onClickSelectCountry(view: View){ //слушатель нажатий для текст вью со странами
        val listCountry = CityHelper.getAllCountries(this) //получаем список стран
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry) //когда жмем тв контри, передаем тв контри
        if (rootElement.tvCity.text.toString() != getString(R.string.select_city)){ //если город уже выбран
            rootElement.tvCity.text = getString(R.string.select_city) //очистить город в текст вью
        }
    }

    fun onClickSelectCity(view: View){ //слушатель нажатий для текст вью с городами
        val selectedCountry = rootElement.tvCountry.text.toString() //получаем выбранную в текст вью страну
        if (selectedCountry != getString(R.string.select_country)) {//если еще не выбрана страна то запускаем этот код
            val listCity = CityHelper.getAllCities(selectedCountry, this) //получаем список городов
            dialog.showSpinnerDialog(this, listCity, rootElement.tvCity) //запускаем диалог
        } else {
            Toast.makeText(this, "No country selected", Toast.LENGTH_LONG).show()
        }
    }

}
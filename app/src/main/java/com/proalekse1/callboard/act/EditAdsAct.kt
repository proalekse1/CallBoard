package com.proalekse1.callboard.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.proalekse1.callboard.R
import com.proalekse1.callboard.databinding.ActivityEditAdsBinding
import com.proalekse1.callboard.dialogs.DialogSpinnerHelper
import com.proalekse1.callboard.utils.CityHelper

class EditAdsAct : AppCompatActivity() { //активити для новых объявлений
    private lateinit var rootElement:ActivityEditAdsBinding //для байндинга
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater) //для байндинга
        setContentView(rootElement.root) //для байндинга
        val listCountry = CityHelper.getAllCountries(this) //получаем список стран

        val dialog = DialogSpinnerHelper() //инициализируем диалог
        dialog.showSpinnerDialog(this, listCountry) //запускаем диалог

    }
}
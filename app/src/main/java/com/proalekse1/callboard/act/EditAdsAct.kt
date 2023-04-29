package com.proalekse1.callboard.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proalekse1.callboard.R
import com.proalekse1.callboard.databinding.ActivityEditAdsBinding

class EditAdsAct : AppCompatActivity() { //активити для новых объявлений
    private lateinit var rootElement:ActivityEditAdsBinding //для байндинга
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater) //для байндинга
        setContentView(rootElement.root) //для байндинга
    }
}
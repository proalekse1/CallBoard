package com.proalekse1.callboard.frag

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.proalekse1.callboard.R
import com.proalekse1.callboard.databinding.ListImageFragBinding

open class BaseAdsFrag: Fragment(), InterAdsClose { //базовый фрагмент

    lateinit var adView: AdView
    var interAd: InterstitialAd? = null //другая реклама


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInterAd() //запускаем вторую рекламу
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }

    private fun initAds(){ //инициализируем рекламный баннер
        MobileAds.initialize(activity as Activity)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun loadInterAd(){ //вторая реклама, тут просто ее подгружаем

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context as Activity, getString(R.string.ad_inter_id), adRequest, object: InterstitialAdLoadCallback() { //идентификатор рекламы

            override fun onAdLoaded(ad: InterstitialAd) {

                interAd = ad
            }

        })
    }

    fun showInterAd(){ //функция для показа второй рекламы

        if(interAd != null){

            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){ //этот колбак будет следить за рекламой

                override fun onAdDismissedFullScreenContent() {

                    onClose() //когда пользователь закроет фрагмент реклама закроется
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) { //отслеживаем ошибку показа рекламы

                    onClose()
                }
            }

            interAd?.show(activity as Activity) //показываем рекламу

        } else { //если реклама налл то закроется чтобы не заблокировался экран

            onClose()
        }
    }


    override fun onClose() {}

}
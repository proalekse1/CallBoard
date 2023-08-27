package com.proalekse1.callboard.utils


import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix


object ImagePicker { // в этом классе будем получать картинки
    const val MAX_IMAGE_COUNT = 3 //максимальное количество картинок
    const val REQUES_CODE_GET_IMAGES = 999
    const val REQUES_CODE_GET_SINGL_IMAGE = 998 //для выбора одной картинки
    fun getImages(context: AppCompatActivity, imageCounter : Int, rCode : Int){
        val options = Options.init()
            .setRequestCode(rCode) //код запроса
            .setCount(imageCounter)                 //максимальное количество картинок для выбора
            .setFrontfacing(false)                  //возможность использования фронтальной камеры
            .setMode(Options.Mode.Picture)          //что можем выбирать видео или картинку или все
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //выбор ориентации экрана
            .setPath("/pix/images")

        Pix.start(context, options)
    }
}
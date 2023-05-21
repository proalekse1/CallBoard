package com.proalekse1.callboard.utils



import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix


object ImagePicker { // в этом классе будем получать картинки
    const val REQUES_CODE_GET_IMAGES = 999
    fun getImages(context: AppCompatActivity){
        val options = Options.init()
            .setRequestCode(REQUES_CODE_GET_IMAGES) //код запроса
            .setCount(3)                            //максимальное количество картинок для выбора
            .setFrontfacing(false)                  //возможность использования фронтальной камеры
            .setMode(Options.Mode.Picture)          //что можем выбирать видео или картинку или все
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //выбор ориентации экрана
            .setPath("/pix/images")

        Pix.start(context, options)
    }
}
package com.proalekse1.callboard.utils


import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.proalekse1.callboard.act.EditAdsAct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


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

    fun showSelectedImages(resultCode: Int, requestCode: Int, data: Intent?, edAct: EditAdsAct){ //функция выбора картинки

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == ImagePicker.REQUES_CODE_GET_IMAGES) {
            if(data != null){

                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                if(returnValues?.size!! > 1 && edAct.chooseImageFrag == null ) { //если нет фрагмента создаем его-первый раз выбираем картинки

                    edAct.openChooseImageFrag(returnValues) //запускаем фрагмент

                } else if (edAct.chooseImageFrag != null){ //если фрагмент уже создан не надо его еще создавать

                    edAct.chooseImageFrag?.updateAdapter(returnValues)

                } else if(returnValues.size == 1 && edAct.chooseImageFrag == null){ //если первый раз выбрана 1 картинка сразу покажем ее во вью пейджере

                    CoroutineScope(Dispatchers.Main).launch{ //корутина

                        edAct.rootElement.pBarLoad.visibility = View.VISIBLE //запускаем прогресс бар
                        val bitMapArray = ImageManager.imageResize(returnValues) as ArrayList<Bitmap> //сжимаем картинку
                        edAct.rootElement.pBarLoad.visibility = View.GONE //скрываем прогресс бар
                        edAct.imageAdapter.update(bitMapArray)
                    }
                }
            }
        } else if(resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUES_CODE_GET_SINGL_IMAGE){

            if(data != null){

                val uris = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                edAct.chooseImageFrag?.setSingleImage(uris?.get(0)!!, edAct.editImagePos)

            }
        }
    }
}
package com.proalekse1.callboard.utils


import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.proalekse1.callboard.act.EditAdsAct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


object ImagePicker { // в этом классе будем получать картинки
    const val MAX_IMAGE_COUNT = 3 //максимальное количество картинок
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUES_CODE_GET_SINGL_IMAGE = 998 //для выбора одной картинки
    private fun getOptions(imageCounter : Int): Options{
        val options = Options.init()
            //43урок устарел .setRequestCode(rCode) //код запроса
            .setCount(imageCounter)                 //максимальное количество картинок для выбора
            .setFrontfacing(false)                  //возможность использования фронтальной камеры
            .setMode(Options.Mode.Picture)          //что можем выбирать видео или картинку или все
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //выбор ориентации экрана
            .setPath("/pix/images")
            return options
        //43урок устарел Pix.start(context, options)
    }

    fun launcher(edAct: EditAdsAct, launcher: ActivityResultLauncher<Intent>?, imageCounter: Int){ //запускаем библиотеку pix
        PermUtil.checkForCamaraWritePermissions(edAct){ //проверка разрешения на камеру
            val intent = Intent(edAct, Pix::class.java).apply{
                putExtra("options", getOptions(imageCounter))
            }
            launcher?.launch(intent)
        }

    }

    fun getLauncherForMultiSelectImages(edAct: EditAdsAct): ActivityResultLauncher<Intent> { //лаунчер для выбора нескольких картинок
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if(result.data != null){
                    val returnValues = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)

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
            }
        }
    }

    fun getLauncherForSingleImage(edAct: EditAdsAct): ActivityResultLauncher<Intent> { //функция выбора картинки
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {

                if (result.data != null) {

                    val uris = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    edAct.chooseImageFrag?.setSingleImage(uris?.get(0)!!, edAct.editImagePos)
                }
            }
        }
    }
}
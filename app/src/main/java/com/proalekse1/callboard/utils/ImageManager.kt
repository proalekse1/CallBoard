package com.proalekse1.callboard.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

object ImageManager { //для редактирования картинок
    private const val MAX_IMAGE_SIZE = 1000 //константа для решения надоли сжимать картинку
    private const val WIDTH = 0 //ширина картинки
    private const val HEIGHT = 1 //длина картинки


    fun getImageSize(uri : String) : List<Int>{ //получаем список с картинками

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true //получаем размер по границам чтобы не забить память
        }
        BitmapFactory.decodeFile(uri, options)
        return if(imageRotation(uri) == 90)
            listOf(options.outHeight, options.outWidth) //параметры которые запишем в список, ширина и высота, меняются местами если вертикально держим телефон

        else listOf(options.outWidth, options.outHeight)
    }

    private fun imageRotation(uri : String) : Int{ //считываем положение экрана
        val rotation : Int
        val imageFile = File(uri)
        val exif = ExifInterface(imageFile.absolutePath) //получили путь к файлу
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL) //получаем ориентацию
        rotation = if(orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270){ //если вертикально держим телефон
            90
        } else { //если держим горизонтально то запишем в значение положения 0
            0
        }
        return rotation
    }

    fun chooseScaleType(im: ImageView, bitMap: Bitmap){ //растягиваем или нет картинку если вертикальная

        if(bitMap.width > bitMap.height){
            im.scaleType = ImageView.ScaleType.CENTER_CROP //растягиваем если горизонтальная
        }else {
            im.scaleType = ImageView.ScaleType.CENTER_INSIDE //ничего не делаем если вертикальная
        }
    }

    suspend fun imageResize(uris: List<String>) : List<Bitmap> = withContext(Dispatchers.IO){ //функция уменшения картинки + корутина
        val tempList = ArrayList<List<Int>>() //массив в котором будет высота и ширина
        val bitmapList = ArrayList<Bitmap>() //массив в котором будет bitmap
        for (n in uris.indices){ //перебираем массив

            val size = getImageSize(uris[n])
            //Log.d("MyLog", "Width : ${size[WIDTH]} Height ${size[HEIGHT]}") //проверка реальног размера картинки

            val imageRatio = size[WIDTH].toFloat() / size[HEIGHT].toFloat() //делим ширину на высоту и получаем коэффициент уменьшения

            if(imageRatio > 1){ //проверяем какая сторона больше

                if(size[WIDTH] > MAX_IMAGE_SIZE){ //проверяем надо ли вообще уменьшать

                    tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt())) //делаем ширину максимально разрешенную 1000, высоту находим с помощью коэффициента
                } else { //если картинка маленькая, ничего не делаем
                    tempList.add(listOf(size[WIDTH], size[HEIGHT])) //просто записываем размеры
                }

            } else {

                if(size[HEIGHT] > MAX_IMAGE_SIZE){ //проверяем надо ли вообще уменьшать

                    tempList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE)) //делаем ширину максимально разрешенную 1000, высоту находим с помощью коэффициента
                } else { //если картинка маленькая, ничего не делаем
                    tempList.add(listOf(size[WIDTH], size[HEIGHT])) //просто записываем размеры
                }

            }
            //Log.d("MyLog", "Width : ${tempList[n][WIDTH]} Height ${tempList[n][HEIGHT]}")
            //Log.d("MyLog", "Ratio : $imageRatio") //проверка коэффициента

        } //заканчивается цикл

        for(i in uris.indices) { //берем циклом картинки из массива по одной

         val e = kotlin.runCatching { //специальный блок который выдаст exception(исключение), выдает успех или неуспех

              bitmapList.add(Picasso.get().load(File(uris[i])).resize(tempList[i][WIDTH], tempList[i][HEIGHT]).get()) //это трудоемкая функция которая будет блокировать, поэтому ее мы запускаем на второстепенном потоке

          }

            Log.d("MyLog", "Bitmap load done: ${e.isSuccess}") //проверка исключения

        }
        //delay(10000)//задержка пример трудоемкой операции
        return@withContext bitmapList // возвращаем список bitmap
    }

}
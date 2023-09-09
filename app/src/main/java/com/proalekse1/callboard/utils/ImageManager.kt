package com.proalekse1.callboard.utils

import android.graphics.BitmapFactory
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.File

object ImageManager { //для редактирования картинок
    const val MAX_IMAGE_SIZE = 1000 //константа для решения надоли сжимать картинку
    const val WIDTH = 0 //ширина картинки
    const val HEIGHT = 1 //длина картинки


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

    fun imageResize(uris: List<String>){ //функция уменшения картинки
        val tempList = ArrayList<List<Int>>() //массив в котором будет высота и ширина
        for (n in uris.indices){ //перебираем массив

            val size = getImageSize(uris[n])
            Log.d("MyLog", "Width : ${size[WIDTH]} Height ${size[HEIGHT]}") //проверка реальног размера картинки

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
            Log.d("MyLog", "Width : ${tempList[n][WIDTH]} Height ${tempList[n][HEIGHT]}")
            //Log.d("MyLog", "Ratio : $imageRatio") //проверка коэффициента

        } //заканчивается цикл
    }

}
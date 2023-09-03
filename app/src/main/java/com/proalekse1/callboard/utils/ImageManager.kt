package com.proalekse1.callboard.utils

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import java.io.File

object ImageManager { //для редактирования картинок

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

}
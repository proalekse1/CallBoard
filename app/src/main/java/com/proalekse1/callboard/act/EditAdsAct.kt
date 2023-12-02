package com.proalekse1.callboard.act


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.fxn.utility.PermUtil
import com.proalekse1.callboard.R
import com.proalekse1.callboard.adapters.ImageAdapter
import com.proalekse1.callboard.data.Ad
import com.proalekse1.callboard.database.DbManager
import com.proalekse1.callboard.databinding.ActivityEditAdsBinding
import com.proalekse1.callboard.dialogs.DialogSpinnerHelper
import com.proalekse1.callboard.frag.FragmentCloseInterface
import com.proalekse1.callboard.frag.ImageListFrag
import com.proalekse1.callboard.utils.CityHelper
import com.proalekse1.callboard.utils.ImagePicker


class EditAdsAct : AppCompatActivity(), FragmentCloseInterface { //активити для новых объявлений
    var chooseImageFrag : ImageListFrag? = null //для отслеживания выбрали уже картинку во фрагменте или нет
    lateinit var rootElement:ActivityEditAdsBinding //для байндинга
    private val dialog = DialogSpinnerHelper() //инициализируем диалог
    lateinit var imageAdapter : ImageAdapter //подключаем адаптер
    private val dbManager = DbManager(null) //инициализируем дата менеджер
    var editImagePos = 0 //позиция картинки в массиве
    var launcherMultiSelectImage: ActivityResultLauncher<Intent>? = null //лаунчер для выбора картинок
    var launcherSingleSelectImage: ActivityResultLauncher<Intent>? = null //лаунчер для выбора одной картинки



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater) //для байндинга
        setContentView(rootElement.root) //для байндинга
        init()
    }



    override fun onRequestPermissionsResult( //функцию запроса на доступ к фото на телефоне и к камере
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //если разрешение получено
                    //ImagePicker.getImages(this, 3, ImagePicker.REQUEST_CODE_GET_IMAGES) //получаем фото
                } else {

                        Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) //новый супер колл 43 урок
    }

    private fun init(){
        imageAdapter = ImageAdapter() //инициализирууем адаптер
        rootElement.vpImages.adapter = imageAdapter //находим вью пейджер и подключаем адаптер
        launcherMultiSelectImage = ImagePicker.getLauncherForMultiSelectImages(this) //лаунчер для выбора картинок
        launcherSingleSelectImage = ImagePicker.getLauncherForSingleImage(this) //лаунчер для выбора 1 картинки
    }

    //OnClicks
    fun onClickSelectCountry(view: View){ //слушатель нажатий для текст вью со странами
        val listCountry = CityHelper.getAllCountries(this) //получаем список стран
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry) //когда жмем тв контри, передаем тв контри
        if (rootElement.tvCity.text.toString() != getString(R.string.select_city)){ //если город уже выбран
            rootElement.tvCity.text = getString(R.string.select_city) //очистить город в текст вью
        }
    }

    fun onClickSelectCity(view: View){ //слушатель нажатий для текст вью с городами
        val selectedCountry = rootElement.tvCountry.text.toString() //получаем выбранную в текст вью страну
        if (selectedCountry != getString(R.string.select_country)) {//если еще не выбрана страна то запускаем этот код
            val listCity = CityHelper.getAllCities(selectedCountry, this) //получаем список городов
            dialog.showSpinnerDialog(this, listCity, rootElement.tvCity) //запускаем диалог
        } else {
            Toast.makeText(this, "No country selected", Toast.LENGTH_LONG).show()
        }
    }

    fun onClickSelectCat(view: View){ //слушатель нажатий для текст вью с категориями

        val listCity = resources.getStringArray(R.array.category).toMutableList() as ArrayList //получаем список категорий из массива
            dialog.showSpinnerDialog(this, listCity, rootElement.tvCat) //запускаем диалог

    }

    fun onClickGetImages(view: View){ //слушатель нажатий для кнопки добавить картинку

        if (imageAdapter.mainArray.size == 0){ //если нет фото открываем фотоаппарат
            ImagePicker.launcher(this, launcherMultiSelectImage, 3) //получаем фото
        } else { //если уже выбирали фото открываем фрагмент для редактирования фото

            openChooseImageFrag(null)
            chooseImageFrag?.updateAdapterFromEdit(imageAdapter.mainArray)
        }

    }

    fun onClickPublish(view: View){ //слушатель для кнопки опубликовать

        dbManager.publishAd(fillAd()) //запустили функцию заполнения дата класса

    }

    private fun fillAd(): Ad{ //функция заполнения дата класса
        val ad: Ad //дата класс
        rootElement.apply{
            ad = Ad(tvCountry.text.toString(), //достаем все что ввели при заполнении объявления
                tvCity.text.toString(),
                editTel.text.toString(),
                edIndex.text.toString(),
                checkBoxWithSend.isChecked.toString(),
                tvCat.text.toString(),
                edTitle.text.toString(),
                edPrice.text.toString(),
                edDescription.text.toString(),
                dbManager.db.push().key, dbManager.auth.uid //получаем уникальный ключ из fire base
            )
        }
        return ad //возвращает заполненное объявление
    }

    override fun onFragClose(list : ArrayList<Bitmap>) { //метод интерфейса
        rootElement.scroolViewMain.visibility = View.VISIBLE //покажем вью
        imageAdapter.update(list) //обновляем список
        chooseImageFrag = null
    }

    fun openChooseImageFrag(newList : ArrayList<String>?){ //заменяем холдер на фрагмент

        chooseImageFrag = ImageListFrag(this, newList)
        rootElement.scroolViewMain.visibility = View.GONE //скрываем вью
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(
            R.id.place_holder, chooseImageFrag!!) //заменяем холдер на фрагмент
        fm.commit()
        // ImagePicker.getImages(this)

    }
}
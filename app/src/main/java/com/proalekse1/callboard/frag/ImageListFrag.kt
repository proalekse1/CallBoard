package com.proalekse1.callboard.frag

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R
import com.proalekse1.callboard.databinding.ListImageFragBinding
import com.proalekse1.callboard.dialoghelper.ProgressDialog
import com.proalekse1.callboard.utils.AdapterCallBack
import com.proalekse1.callboard.utils.ImageManager
import com.proalekse1.callboard.utils.ImagePicker
import com.proalekse1.callboard.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFrag(private val fragCloseInterface : FragmentCloseInterface, private val newList : ArrayList<String>?) : Fragment(), AdapterCallBack {
    lateinit var rootElement : ListImageFragBinding //подключили байнднг к фрагменту
    val adapter = SelectImageRvAdapter(this) //подлючили адаптер
    val dragCallback = ItemTouchMoveCallback(adapter) //подключили колбак для перемешивания
    val touchHelper = ItemTouchHelper(dragCallback) //для перемешивания картинок
    private var job: Job? = null
    private var addImageItem: MenuItem? = null //кнопка добаления картинки
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //рисуем фрагмент
        rootElement = ListImageFragBinding.inflate(inflater) //подключили байнднг к фрагменту
        return rootElement.root //подключили байнднг к фрагменту
    }

    override fun onItemDelete() { //функци интерфейса интерфейс
        addImageItem?.isVisible = true //появляется кнопка добавить
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем фрагмент
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar() //запустили тулбар
        //val rcView = view.findViewById<RecyclerView>(R.id.rcViewSelectImage) //находим ресайклер вью старым способом без байндинга
        touchHelper.attachToRecyclerView(rootElement.rcViewSelectImage) //поключили к ресайклер вью
        rootElement.rcViewSelectImage.layoutManager = LinearLayoutManager(activity) //то как будут располагаться картинки
        rootElement.rcViewSelectImage.adapter = adapter //присваиваем адаптер

        if(newList != null){ //если первый раз то запускаем компрессию картинок
            resizeSelectedImages(newList, true)
        }
    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>){
        adapter.updateAdapter(bitmapList, true) //обновляем адаптер
    }

    override fun onDetach() { //фрагмент отсоединяеся от активити
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray) //запускается метод на активити
        job?.cancel()
       /* Log.d("MyLog", "Title 0 : ${adapter.mainArray[0].title}") //проверка перемешивания картинок
        Log.d("MyLog", "Title 1 : ${adapter.mainArray[1].title}")
        Log.d("MyLog", "Title 2 : ${adapter.mainArray[2].title}")*/
    }

    private fun resizeSelectedImages(newList: ArrayList<String>, needClear: Boolean){ //функция для оптимизации корутины
        job = CoroutineScope(Dispatchers.Main).launch {  //запустили менеджер уменьшения картинок в корутине
            val dialog = ProgressDialog.createProgressDialog(activity as Activity) //вставляем прогресс бар
            val bitmapList = ImageManager.imageResize(newList) //конвертация картинки
            dialog.dismiss() //закрываем прогресс бар
            adapter.updateAdapter(bitmapList, needClear) //обновляем адаптер
            if(adapter.mainArray.size > 2) addImageItem?.isVisible = false//проверка надо прятать кнопку плюс или нет
        }
    }

    private fun setUpToolbar(){ //для тулбара
        rootElement.tb.inflateMenu(R.menu.menu_choose_image) //надули меню тулбара
        val deleteItem = rootElement.tb.menu.findItem(R.id.id_delete_image) //создали переменные для кнопок
        addImageItem = rootElement.tb.menu.findItem(R.id.id_add_image) //кнопка добавления картинки

        rootElement.tb.setNavigationOnClickListener { //слушатель для кнопки назад
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit() //закрываем фрагмент
            //Log.d("MyLog","Home item") //проверка
        }

        deleteItem.setOnMenuItemClickListener { //слушатель для удалить
            adapter.updateAdapter(ArrayList(), true) //передаем пустой масси в адаптер таким образом его очищаем
            addImageItem?.isVisible = true //появляется кнопка добавить
            //Log.d("MyLog","Delete item") //проверка
            true
        }

        addImageItem?.setOnMenuItemClickListener { //слушатель для добавить
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size // находим сколько картинок показано
            ImagePicker.getImages(activity as AppCompatActivity, imageCount, ImagePicker.REQUES_CODE_GET_IMAGES)
            //Log.d("MyLog","Add item") //проверка
            true
        }
    }

    fun updateAdapter(newList : ArrayList<String>){ //функция обновления адаптера если картинки уже добавляли а потом удалили
        resizeSelectedImages(newList, false)
    }

    fun setSingleImage(uri : String, pos : Int){ //обновление одной картинки в адаптере
        val pBar = rootElement.rcViewSelectImage[pos].findViewById<ProgressBar>(R.id.pBar) //получаем доступ к одному элементу из адаптера
        job = CoroutineScope(Dispatchers.Main).launch{  //запустили менеджер уменьшения картинок в корутине
            pBar.visibility = View.VISIBLE//делаем прогресс бар видимым
            val bitmapList = ImageManager.imageResize(listOf(uri))
            pBar.visibility = View.GONE//делаем прогресс бар невидимым
            adapter.mainArray[pos] = bitmapList[0]
            adapter.notifyItemChanged(pos) //после обновляем один элемент по позиции
        }
    }


}
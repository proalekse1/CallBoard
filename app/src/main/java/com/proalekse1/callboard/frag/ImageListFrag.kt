package com.proalekse1.callboard.frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R
import com.proalekse1.callboard.databinding.ListImageFragBinding
import com.proalekse1.callboard.utils.ImagePicker
import com.proalekse1.callboard.utils.ItemTouchMoveCallback

class ImageListFrag(private val fragCloseInterface : FragmentCloseInterface, private val newList : ArrayList<String>) : Fragment() {
    lateinit var rootElement : ListImageFragBinding //подключили байнднг к фрагменту
    val adapter = SelectImageRvAdapter() //подлючили адаптер
    val dragCallback = ItemTouchMoveCallback(adapter) //подключили колбак для перемешивания
    val touchHelper = ItemTouchHelper(dragCallback) //для перемешивания картинок
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //рисуем фрагмент
        rootElement = ListImageFragBinding.inflate(inflater) //подключили байнднг к фрагменту
        return rootElement.root //подключили байнднг к фрагменту
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем фрагмент
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar() //запустили тулбар
        //val rcView = view.findViewById<RecyclerView>(R.id.rcViewSelectImage) //находим ресайклер вью старым способом без байндинга
        touchHelper.attachToRecyclerView(rootElement.rcViewSelectImage) //поключили к ресайклер вью
        rootElement.rcViewSelectImage.layoutManager = LinearLayoutManager(activity) //то как будут располагаться картинки
        rootElement.rcViewSelectImage.adapter = adapter //присваиваем адаптер
        adapter.updateAdapter(newList, true)

            //activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit() //закрываем фрагмент

    }

    override fun onDetach() { //фрагмент отсоединяеся от активити
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray) //запускается метод на активити
       /* Log.d("MyLog", "Title 0 : ${adapter.mainArray[0].title}") //проверка перемешивания картинок
        Log.d("MyLog", "Title 1 : ${adapter.mainArray[1].title}")
        Log.d("MyLog", "Title 2 : ${adapter.mainArray[2].title}")*/
    }

    private fun setUpToolbar(){ //для тулбара
        rootElement.tb.inflateMenu(R.menu.menu_choose_image) //надули меню тулбара
        val deleteItem = rootElement.tb.menu.findItem(R.id.id_delete_image) //создали переменные для кнопок
        val addImageItem = rootElement.tb.menu.findItem(R.id.id_add_image)

        rootElement.tb.setNavigationOnClickListener { //слушатель для кнопки назад
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit() //закрываем фрагмент
            //Log.d("MyLog","Home item") //проверка
        }

        deleteItem.setOnMenuItemClickListener { //слушатель для удалить
            adapter.updateAdapter(ArrayList(), true) //передаем пустой масси в адаптер таким образом его очищаем
            //Log.d("MyLog","Delete item") //проверка
            true
        }

        addImageItem.setOnMenuItemClickListener { //слушатель для добавить
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size // находим сколько картинок показано
            ImagePicker.getImages(activity as AppCompatActivity, imageCount)
            //Log.d("MyLog","Add item") //проверка
            true
        }
    }

    fun updateAdapter(newList : ArrayList<String>){ //функция обновления адаптера если картинки уже добавляли

        adapter.updateAdapter(newList, false)
    }

}
package com.proalekse1.callboard.frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R
import com.proalekse1.callboard.utils.ItemTouchMoveCallback

class ImageListFrag(private val fragCloseInterface : FragmentCloseInterface, private val newList : ArrayList<String>) : Fragment() {
    val adapter = SelectImageRvAdapter() //подлючили адаптер
    val dragCallback = ItemTouchMoveCallback(adapter) //подключили колбак для перемешивания
    val touchHelper = ItemTouchHelper(dragCallback) //для перемешивания картинок
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //рисуем фрагмент
        return inflater.inflate(R.layout.list_image_frag, container, false) //надули разметку
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем фрагмент
        super.onViewCreated(view, savedInstanceState)
        val bBack = view.findViewById<Button>(R.id.bBack) //находим кнопку назад
        val rcView = view.findViewById<RecyclerView>(R.id.rcViewSelectImage) //находим ресайклер вью
        touchHelper.attachToRecyclerView(rcView) //поключили к ресайклер вью
        rcView.layoutManager = LinearLayoutManager(activity) //то как будут располагаться картинки
        rcView.adapter = adapter //присваиваем адаптер
        val updateList = ArrayList<SelectImageItem>()
        for(n in 0 until newList.size){
            updateList.add(SelectImageItem(n.toString(), newList[n])) //записываем в дата класс ВАРИАНТ1
        }
        adapter.updateAdapter(updateList)
        bBack.setOnClickListener { //слушатель нажатий
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit() //закрываем фрагмент
        }
    }

    override fun onDetach() { //фрагмент отсоединяеся от активити
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray) //запускается метод на активити
        Log.d("MyLog", "Title 0 : ${adapter.mainArray[0].title}") //проверка перемешивания картинок
        Log.d("MyLog", "Title 1 : ${adapter.mainArray[1].title}")
        Log.d("MyLog", "Title 2 : ${adapter.mainArray[2].title}")
    }
}
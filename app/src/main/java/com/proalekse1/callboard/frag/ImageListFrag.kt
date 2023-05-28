package com.proalekse1.callboard.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R

class ImageListFrag(private val fragCloseInterface : FragmentCloseInterface, private val newList : ArrayList<String>) : Fragment() {
    val adapter = SelectImageRvAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //рисуем фрагмент
        return inflater.inflate(R.layout.list_image_frag, container, false) //надули разметку
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем фрагмент
        super.onViewCreated(view, savedInstanceState)
        val bBack = view.findViewById<Button>(R.id.bBack) //находим кнопку назад
        val rcView = view.findViewById<RecyclerView>(R.id.rcViewSelectImage) //находим ресайклер вью
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
        fragCloseInterface.onFragClose() //запускается метод на активити
    }
}
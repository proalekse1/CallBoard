package com.proalekse1.callboard.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.proalekse1.callboard.R

class ImageListFrag(val fragCloseInterface : FragmentCloseInterface) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //рисуем фрагмент
        return inflater.inflate(R.layout.list_image_frag, container, false) //надули разметку
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //создаем фрагмент
        super.onViewCreated(view, savedInstanceState)
        val bBack = view.findViewById<Button>(R.id.bBack) //находим кнопку назад
        bBack.setOnClickListener { //слушатель нажатий
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit() //закрываем фрагмент
        }
    }

    override fun onDetach() { //фрагмент отсоединяеся от активити
        super.onDetach()
        fragCloseInterface.onFragClose() //запускается метод на активити
    }
}
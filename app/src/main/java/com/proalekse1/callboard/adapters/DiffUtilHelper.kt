package com.proalekse1.callboard.adapters

import androidx.recyclerview.widget.DiffUtil
import com.proalekse1.callboard.model.Ad

class DiffUtilHelper(val oldList: List<Ad>, val newList: List<Ad>): DiffUtil.Callback() { //класс для удаления с анимацией

    override fun getOldListSize(): Int { //размер старого списка
        return oldList.size
    }

    override fun getNewListSize(): Int { //размер нового списка
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].key == newList[newItemPosition].key //сравнение ключей по позиции объявления
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition] //сравнение объявлений по позиции
    }
}
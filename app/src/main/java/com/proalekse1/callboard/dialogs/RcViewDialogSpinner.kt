package com.proalekse1.callboard.dialogs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R

class RcViewDialogSpinner: RecyclerView.Adapter<RcViewDialogSpinner.SpViewHolder>() { //для списка стран
    private val mainList = ArrayList<String>() //создаем массив стран в этом классе

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpViewHolder { //рисуем разметку одного элемента
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false) //надули разметку одного элемента
        return SpViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpViewHolder, position: Int) { //пишем текст в элементе холдера
        holder.setData(mainList[position])
    }

    override fun getItemCount(): Int { //указываем коичество элементов
      return mainList.size
    }

    class SpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //создаем вью холдер чтобы найти текст вью

        fun setData(text : String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.tvSpItem) //ищем текст вью
            tvSpItem.text = text
        }
    }

    fun updateAdapter(list : ArrayList<String>){ //заполняем массив
        mainList.clear() //очищаем массив если чтото было
        mainList.addAll(list)
        notifyDataSetChanged()
    }
}
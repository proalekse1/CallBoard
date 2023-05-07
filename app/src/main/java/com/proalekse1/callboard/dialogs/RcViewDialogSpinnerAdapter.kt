package com.proalekse1.callboard.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R
import com.proalekse1.callboard.act.EditAdsAct

class RcViewDialogSpinnerAdapter(var context: Context, var dialog:AlertDialog): RecyclerView.Adapter<RcViewDialogSpinnerAdapter.SpViewHolder>() { //для списка стран
    private val mainList = ArrayList<String>() //создаем массив стран в этом классе

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpViewHolder { //рисуем разметку одного элемента
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false) //надули разметку одного элемента
        return SpViewHolder(view, context, dialog)
    }

    override fun onBindViewHolder(holder: SpViewHolder, position: Int) { //пишем текст в элементе холдера
        holder.setData(mainList[position])
    }

    override fun getItemCount(): Int { //указываем коичество элементов
      return mainList.size
    }

    class SpViewHolder(itemView: View, var context: Context, var dialog: AlertDialog) : RecyclerView.ViewHolder(itemView), View.OnClickListener { //создаем вью холдер чтобы найти текст вью
        private var itemText = ""
        fun setData(text : String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.tvSpItem) //ищем текст вью
            tvSpItem.text = text
            itemText = text
            itemView.setOnClickListener (this) //подключили слушатель нажатий к каждому элементу списка стран
        }

        override fun onClick(v: View?) { //слушатель нажатий списка стран
            (context as EditAdsAct).rootElement.tvCountry.text = itemText //делаем каст и говорим что контекст это активити EditAdsAct
            dialog.dismiss() //закрываем диалог
        }
    }

    fun updateAdapter(list : ArrayList<String>){ //заполняем массив
        mainList.clear() //очищаем массив если чтото было
        mainList.addAll(list)
        notifyDataSetChanged()
    }
}
package com.proalekse1.callboard.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R
import com.proalekse1.callboard.frag.SelectImageItem

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    val mainArray = ArrayList<SelectImageItem>() //массив для картинок

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_adapter_item, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position].imageUri) //берем только юрл изи массива
    }

    override fun getItemCount(): Int {
        return mainArray.size //передаем размер массива
    }

    class ImageHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
       lateinit var imItem : ImageView
        fun setData(uri : String){ //ссылка на картинку
            imItem = itemView.findViewById(R.id.imItem) //находим элемент
            imItem.setImageURI(Uri.parse(uri)) //находим картинку по юрл через парсинг
        }
    }

    fun update(newList : ArrayList<SelectImageItem>){ //функция обновления массива
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

}
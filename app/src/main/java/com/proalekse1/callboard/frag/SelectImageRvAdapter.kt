package com.proalekse1.callboard.frag

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R
import com.proalekse1.callboard.utils.ItemTouchMoveCallback

class SelectImageRvAdapter : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter { //для показа картинок во фрагменте
    val mainArray = ArrayList<SelectImageItem>() //массив для адаптера который хранит дата класс
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder { //создаем элемент
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_frag_item, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
      return  mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) { //метод интерфейса функции перемешиания
        val targetItem = mainArray[targetPos] //сохраняем элемент над которым занесли захваченную картинку, чтобы когда захваченная картинка ляжет на него первый не исчез
        mainArray[targetPos] = mainArray[startPos] //меняем местами картинки
        mainArray[startPos] = targetItem //старый сохраненный элемент кидаем на начальную позицию
        notifyItemMoved(startPos, targetPos) //говорим адаптеру о изменениях
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //холдер заполняет
        lateinit var tvTitle : TextView
        lateinit var image : ImageView
        fun setData(item : SelectImageItem){
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageView)
            tvTitle.text = item.title //записываем из дата класса в элемент разметки
            image.setImageURI(Uri.parse(item.imageUri)) //превращаем парсингом стринг в юрл
        }
    }
    fun updateAdapter(newList : List<SelectImageItem>){
        mainArray.clear() //очищаем
        mainArray.addAll(newList) //заполняем
        notifyDataSetChanged() //сообщаем что данные изменились
    }

}
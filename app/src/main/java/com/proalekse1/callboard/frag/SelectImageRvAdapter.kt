package com.proalekse1.callboard.frag

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R
import com.proalekse1.callboard.act.EditAdsAct
import com.proalekse1.callboard.utils.ImagePicker
import com.proalekse1.callboard.utils.ItemTouchMoveCallback

class SelectImageRvAdapter : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter { //для показа картинок во фрагменте
    val mainArray = ArrayList<String>() //массив для адаптера который хранит дата класс
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder { //создаем элемент
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_frag_item, parent, false)
        return ImageHolder(view, parent.context, this)
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
        // val titleStart = mainArray[targetPos].title //Теперь заголовок зажатой позиции стерт ее надо сохранить и отдать.
        // mainArray[targetPos].title = targetItem.title //перезаписываем заголовок для стартовой позиции заголовок целевой который был
        mainArray[startPos] = targetItem //старый сохраненный элемент кидаем на начальную позицию
        // mainArray[startPos].title = titleStart //передали старый зголовок
        notifyItemMoved(startPos, targetPos) //говорим адаптеру о изменениях

    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(itemView: View, val context: Context, val adapter: SelectImageRvAdapter) : RecyclerView.ViewHolder(itemView) { //холдер заполняет
        lateinit var tvTitle : TextView
        lateinit var image : ImageView
        lateinit var imEditImage : ImageButton //кнопка редактирования картинки
        lateinit var imDeleteImage : ImageButton //кнопка удаления картинки

        fun setData(item : String){
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageView)
            imEditImage = itemView.findViewById(R.id.imEditImage) //нашли кнопку редактирования картинки
            imDeleteImage = itemView.findViewById(R.id.imDelete) //нашли кнопку удаления картинки

            imEditImage.setOnClickListener { //слушатель нажатий на кнопку редактирования

                ImagePicker.getImages(context as EditAdsAct, 1, ImagePicker.REQUES_CODE_GET_SINGL_IMAGE)
                context.editImagePos = adapterPosition //получаем позицию картинки
            }

            imDeleteImage.setOnClickListener { //слушатель нажатий на кнопку удаления картинки

                adapter.mainArray.removeAt(adapterPosition) //удалили картинку из массива по позиции
                adapter.notifyItemRemoved(adapterPosition) //сообщили адаптеру что удалили
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n) //обновляем позиции после удаления картинки

            }

            tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition] //получаем из массива с именами фоток
            image.setImageURI(Uri.parse(item)) //превращаем парсингом стринг в юрл
        }
    }
    fun updateAdapter(newList : List<String>, needClear : Boolean){
        if(needClear) mainArray.clear() //очищаем список с картинками если надо
        mainArray.addAll(newList) //заполняем
        notifyDataSetChanged() //сообщаем что данные изменились
    }

}
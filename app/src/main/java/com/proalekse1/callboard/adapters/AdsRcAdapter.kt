package com.proalekse1.callboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.data.Ad
import com.proalekse1.callboard.databinding.AdListItemBinding

class AdsRcAdapter : RecyclerView.Adapter<AdsRcAdapter.AdHolder>() { //ресайклер вью адаптер для показа объявлений
    val adArray = ArrayList<Ad>() //массив для хранения объявлений

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder { //создать
        val binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context)) //надули байндинг
        return AdHolder(binding)
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) { //связать view по позициям с нулевой
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int { //получить количество элементов в массиве объявлений, если количесто изменилось то надо дорисовать
       return adArray.size
    }

    fun updateAdapter(newList: List<Ad>){ //обновляем адаптер
        adArray.clear() //очищаем
        adArray.addAll(newList) //добавляем новые элементы
        notifyDataSetChanged()
    }

    class AdHolder(val binding: AdListItemBinding) : RecyclerView.ViewHolder(binding.root) { //холдер будет удерживать в памяти созданные вью

        fun setData(ad: Ad){ //функция которая будт заполнять из дата класса объявления
            binding.apply{
                tvDescription.text = ad.description
                tvPrice.text = ad.price

            }
        }

    }


}
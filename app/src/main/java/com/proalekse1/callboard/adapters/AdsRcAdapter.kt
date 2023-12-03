package com.proalekse1.callboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.proalekse1.callboard.model.Ad
import com.proalekse1.callboard.databinding.AdListItemBinding

class AdsRcAdapter(val auth: FirebaseAuth) : RecyclerView.Adapter<AdsRcAdapter.AdHolder>() { //ресайклер вью адаптер для показа объявлений
    val adArray = ArrayList<Ad>() //массив для хранения объявлений

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder { //создать
        val binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) //надули байндинг
        return AdHolder(binding, auth)
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

    class AdHolder(val binding: AdListItemBinding, val auth: FirebaseAuth) : RecyclerView.ViewHolder(binding.root) { //холдер будет удерживать в памяти созданные вью

        fun setData(ad: Ad){ //функция которая будт заполнять из дата класса объявления
            binding.apply{
                tvDescription.text = ad.description
                tvPrice.text = ad.price
                tvTitle.text = ad.title
            }
            showEditPanel(isOwner(ad))
        }

        private fun isOwner(ad: Ad): Boolean{ //функция проверки uid чтобы понимать объявление собственника или нет
            return ad.uid == auth.uid //ad.uid-идентификатор объяления при создании; auth.uid-идентификатор пользователя
        }

        private fun showEditPanel(isOwner: Boolean){ //показываем или прячем панель редактироания объявления
            if(isOwner){
                binding.editPanel.visibility = View.VISIBLE // видима
            } else {
                binding.editPanel.visibility = View.GONE // невидима
            }
        }

    }

}
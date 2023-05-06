package com.proalekse1.callboard.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proalekse1.callboard.R
import com.proalekse1.callboard.utils.CityHelper

class DialogSpinnerHelper { //диалог для поиска стран и городов

    fun showSpinnerDialog(context: Context, list:ArrayList<String>){
        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null) //подключаем инфлейтер через контекст надуваем разметку
        val adapter = RcViewDialogSpinner() //передаем адаптер
        val rcView = rootView.findViewById<RecyclerView>(R.id.rcSpView) //нашли ресайклер вью на разметке
        val sv = rootView.findViewById<SearchView>(R.id.svSpinner) //нашли серч вью на разметке
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter //подключили к ресайклер вью адаптер
        builder.setView(rootView)
        adapter.updateAdapter(list) //передаем список городов
        setSearchView(adapter, list, sv) //обновляем диалог хелпер после поиска и показыаем одну страну
        builder.show() //диалог нарисует разметку
    }

    private fun setSearchView(adapter: RcViewDialogSpinner, list: ArrayList<String>, sv: SearchView?) { //обновляем диалог хелпер после поиска и показыаем одну страну
        sv?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{ //слушатель изменения текста
            override fun onQueryTextSubmit(p0: String?): Boolean {
               return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val tempList = CityHelper.filterListData(list, newText)
                adapter.updateAdapter(tempList)
                return true
            }
        })
    }


}
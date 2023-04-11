package com.proalekse1.callboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.proalekse1.callboard.databinding.ActivityMainBinding
import com.proalekse1.callboard.dialoghelper.DialogConst
import com.proalekse1.callboard.dialoghelper.DialogHelper


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var rootElement:ActivityMainBinding //подключаем байндинг
    private var dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance() //инициализировали Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater) //надули байндинг
        val view = rootElement.root //подключаем байндинг
        setContentView(view) //подключаем байндинг
        init()
    }

    private fun init(){

        val toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout, rootElement.mainContent.toolbar, R.string.open, R.string.close) //кнопка в тулбаре открытия меню
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener (this) //подключаем к навигатион вью слушатель нажатий
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { //слушатель для кнопок меню
        when(item.itemId){

            R.id.id_my_ads ->{

                Toast.makeText(this, "Presed id_my_ads", Toast.LENGTH_LONG).show()// тостик для проверки кнопки

            }
            R.id.id_car ->{

            }
            R.id.id_pc ->{

            }
            R.id.id_smart ->{

            }
            R.id.id_dm ->{

            }
            R.id.id_sign_up ->{
                //если в меню нажали регистрация то передаем в диалог индекс для регистрациии показывае диалог регистрации
                dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
            }
            R.id.id_sign_in ->{
                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
            }
            R.id.id_sign_out ->{

            }
        }
        rootElement.drawerLayout.closeDrawer(GravityCompat.START) //закрыть меню после нажатия на кнопку
        return true //нужно для when
    }
}
package com.proalekse1.callboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_content.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init(){

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) //кнопка в тулбаре открытия меню
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener (this) //подключаем к навигатион вью слушатель нажатий
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

            }
            R.id.id_sign_in ->{

            }
            R.id.id_sign_out ->{

            }
            R.id.id_my_ads ->{

            }

        }
        drawerLayout.closeDrawer(GravityCompat.START) //закрыть меню после нажатия на кнопку
        return true //нужно для when
    }
}
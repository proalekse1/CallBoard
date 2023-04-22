package com.proalekse1.callboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.proalekse1.callboard.databinding.ActivityMainBinding
import com.proalekse1.callboard.dialoghelper.DialogConst
import com.proalekse1.callboard.dialoghelper.DialogHelper
import com.proalekse1.callboard.dialoghelper.GoogleAccConst


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var tvAccount: TextView //для доступа к хидеру
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //для входа в гугл аккаунт
        if(requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE){
           // Log.d("MyLog", "Sign in result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data) //получить аккаунт
            try{ //пытаемся взять аккаунт и если он выйдет получим ответ почему через catch

                val account = task.getResult(ApiException::class.java) //пытаемся получить и следим за ошибками с помощью ApiException
                if (account != null){
                    Log.d("MyLog", "Api 0") //ловим ошибку 12500
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!) //получаем доступ к токену, !! знаем что не налл
                }

            }catch (e:ApiException){
                Log.d("MyLog", "Api error : ${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun init(){

        val toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout, rootElement.mainContent.toolbar, R.string.open, R.string.close) //кнопка в тулбаре открытия меню
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener (this) //подключаем к навигатион вью слушатель нажатий
        tvAccount = rootElement.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail) //получаем доступ к хидеру
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
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
                uiUpdate(null) //если нажали выход то будет null
                mAuth.signOut() //функция выхода из аккаунта
            }
        }
        rootElement.drawerLayout.closeDrawer(GravityCompat.START) //закрыть меню после нажатия на кнопку
        return true //нужно для when
    }

    fun uiUpdate(user:FirebaseUser?){ //показываем в хидере пользователя который вошел
        tvAccount.text = if(user == null){
            resources.getString(R.string.not_reg) //если поьзователя нет то показать это в хидере
        } else {
            user.email
        }
    }

}
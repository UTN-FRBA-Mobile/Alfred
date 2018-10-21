package com.botigocontigo.alfred

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login_registrarse.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }

        btn_login_ingresar.setOnClickListener {
            startActivity(Intent(this, SplashActivity::class.java))
        }
    }
}

package com.botigocontigo.alfred

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import android.content.Intent
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        bindButtons()
    }

    private fun bindButtons() {
        btnMenu.setOnClickListener {
            goToActivity(MenuActivity::class.java)
        }
        btnLearn.setOnClickListener {
            goToActivity(LearnActivity::class.java)
        }
        btnRisk.setOnClickListener {
            goToActivity(RiskActivity::class.java)
        }
        btnAreas.setOnClickListener {
            goToActivity(AreasActivity::class.java)
        }
        btnChat.setOnClickListener {
            goToActivity(ChatActivity::class.java)
        }
        btnFoda.setOnClickListener {
            goToActivity(FodaActivity::class.java)
        }
        btnTask.setOnClickListener {
//            goToActivity(TasksListActivity::class.java)
        }
        btnLogin.setOnClickListener {
            goToActivity(Login::class.java)
        }


    }

    private fun goToActivity(klass: Class<*>) {
        startActivity(Intent(this, klass))
    }
}


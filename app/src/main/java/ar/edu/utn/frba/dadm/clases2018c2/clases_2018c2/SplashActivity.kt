package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.preferences.MyPreferences
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(!MyPreferences.getUsername(this).isNullOrEmpty()){
            continueToMain()
        }

        continuar.setOnClickListener(View.OnClickListener {
            MyPreferences.setUsername(this, username.text.toString())

            continueToMain()
        })
    }

    private fun continueToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

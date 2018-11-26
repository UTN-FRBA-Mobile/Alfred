package com.botigocontigo.alfred

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject

class Login : AppCompatActivity() {

    companion object {
        val LOG_TAG = "Alfred: "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mypreference = MyPreferences(this)

        btn_login_ingresar.setOnClickListener {
            /**
             * Llamada POST que envia un JSONObject y devuelve un JSONobject
             */


            Log.i(LOG_TAG, "jsonObjectRequestPost")

            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)

            val url = "http://178.128.229.73:3300/methods/api.login/"

            Log.i(LOG_TAG, user_name.text.toString())
            Log.i(LOG_TAG, user_password.text.toString())
            // Log.i(LOG_TAG, url)

            val jsonObject = JSONObject()
            jsonObject.put("email", user_name.text.toString())
            jsonObject.put("password", user_password.text.toString())

            Log.i(LOG_TAG, "JsonObject es: $jsonObject")
            // Request a JSONObject response from the provided URL.
            val jsonObjectRequest = JsonObjectRequest(url, jsonObject,
                    Response.Listener { response ->
                        Log.i(LOG_TAG, "Response es: $response")


                        // Aca se tiene que guardar los valores de la Share Preferen
                        mypreference.setUserEmail(user_name.text.toString())
                        val valor = mypreference.getUserEmail()
                        Log.i(LOG_TAG, "El valor es: $valor")
                        Toast.makeText(this, valor, Toast.LENGTH_LONG).show()

                        //Log.i(LOG_TAG, "Response email es: $response.email")
                        startActivity(Intent(this, SplashActivity::class.java))
                    },
                    Response.ErrorListener { error ->
                        error.printStackTrace()
                        Log.e(LOG_TAG, "Error al Login.")
                    }
            )

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)

        }

        btn_login_registrarse.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }

    }
}

package com.botigocontigo.alfred

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONObject

class SignIn : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btn_sing_in_ingresar.setOnClickListener {
             startActivity(Intent(this, Login::class.java))
        }

        btn_sing_in_registrarse.setOnClickListener {
            // startActivity(Intent(this, Login::class.java))

            /**
             * Llamada POST que envia un JSONObject y devuelve un JSONobject


            Log.i(Login.LOG_TAG, user_nombre.text.toString())
            Log.i(Login.LOG_TAG, user_apellido.text.toString())
            Log.i(Login.LOG_TAG, user_email.text.toString())
            Log.i(Login.LOG_TAG, "jsonObjectRequestPost")
             */

            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)

            val url = "http://178.128.229.73:3300/methods/api.RegisterUser"

            Log.i(Login.LOG_TAG, url)

            val jsonObject = JSONObject()
            jsonObject.put("name", user_nombre.text.toString())
            jsonObject.put("password",  user_contraseña.text.toString())
            jsonObject.put("email",  user_email.text.toString())

            Log.i(Login.LOG_TAG, "JsonObject es: $jsonObject")
            // Request a JSONObject response from the provided URL.
            val jsonObjectRequest = JsonObjectRequest(url, jsonObject,
                    Response.Listener { response ->
                        Log.i(Login.LOG_TAG, "Response es: $response")
                        startActivity(Intent(this, Login::class.java))
                    },
                    Response.ErrorListener { error ->
                        error.printStackTrace()
                        Log.e(Login.LOG_TAG, "Error al registrarse.")
                    }
            )

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        }
    }
}

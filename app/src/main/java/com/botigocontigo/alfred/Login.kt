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
import com.botigocontigo.alfred.backend.UserShareDeserealizer
import com.google.gson.GsonBuilder
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
            execLogin(mypreference)

        }

        btn_login_registrarse.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }

    }

    fun execLogin(mypreference: MyPreferences) {
        try {
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
                        try {
                            // Log.i(LOG_TAG, "Response es: $response")

                            val gsonBuilder = GsonBuilder().serializeNulls()
                            gsonBuilder.registerTypeAdapter(UserShare::class.java, UserShareDeserealizer())
                            val gson = gsonBuilder.create()

                            val userShareJsonParsed: UserShare = gson.fromJson(response.toString(), UserShare::class.java)
                            if (!userShareJsonParsed.sucess!!) throw Exception("Not Logued In");
                            // use userShareJsonParsed.email and userShareJsonParsed.userId with shared preferences
                            Log.i(LOG_TAG, "userShareJsonParsed.email es:" + userShareJsonParsed.email)
                            // Log.i(LOG_TAG, "userShareJsonParsed.userId es:" +  userShareJsonParsed.userId)

                            //Se guardan valores del Email en Share Preferen
                            mypreference.setUserEmail(userShareJsonParsed.email!!)
                            val userEmail = mypreference.getUserEmail()
                            Log.i(LOG_TAG, "El email es: $userEmail")
                            Toast.makeText(this, "Bienvenido!", Toast.LENGTH_LONG).show()


                            //Se guardan valores del Email en Share Preferen
                            mypreference.setUserId(userShareJsonParsed.userId!!)
                            val userID = mypreference.getUserId()
                            Log.i(LOG_TAG, "El userID es: $userID")
                            // Toast.makeText(this, valor2, Toast.LENGTH_LONG).show()


                            startActivity(Intent(this, MenuActivity::class.java))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e(LOG_TAG, "Error al Login.")
                            var errorString = "Compruebe su conexión a internet, el email y la password sean correctas"
                            Toast.makeText(this, errorString, Toast.LENGTH_LONG).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        error.printStackTrace()
                        Log.e(LOG_TAG, "Error al Login.")
                        var errorString = "Compruebe su conexión a internet, el email y la password sean correctas"
                    }
            )

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(LOG_TAG, "Error al Login.")
            var errorString = "Compruebe su conexión a internet, el email y la password sean correctas"
        }
    }
}

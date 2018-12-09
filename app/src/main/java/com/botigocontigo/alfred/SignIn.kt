package com.botigocontigo.alfred

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.botigocontigo.alfred.backend.UserShareDeserealizer
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONObject
import android.text.TextUtils


class SignIn : AppCompatActivity() {

    private val URL_BASE = "http://178.128.229.73:3300/methods/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btn_sing_in_ingresar.setOnClickListener {
             startActivity(Intent(this, Login::class.java))
        }

        btn_sing_in_registrarse.setOnClickListener {
            if(isSomeFieldEmpty()){
                Toast.makeText(this, R.string.ERROR_EMPTY_FIELDS, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if( emailIsInvalid(user_email.text) ) {

                Toast.makeText(this, R.string.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(passwordsNotEqual()){
                Toast.makeText(this, R.string.ERROR_PASSWORD_NOT_EQUALS, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            /**
             * Llamada POST que envia un JSONObject y devuelve un JSONobject
             */

            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)

            val url =  URL_BASE + "api.RegisterUser"

            Log.i(Login.LOG_TAG, url)

            val jsonObject = JSONObject()
            jsonObject.put("name", user_nombre.text.toString())
            jsonObject.put("password",  user_password_signIn.text.toString())
            jsonObject.put("email",  user_email.text.toString())

            Log.i(Login.LOG_TAG, "JsonObject es: $jsonObject")
            // Request a JSONObject response from the provided URL.
            val jsonObjectRequest = JsonObjectRequest(url, jsonObject,
                    Response.Listener { response ->
                        Log.i(Login.LOG_TAG, "Response es: $response")
                        automaticLogIn(user_email.text.toString(), user_password_signIn.text.toString() );
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

    private fun passwordsNotEqual() =
            user_password_signIn.text.toString() != user_password_signIn_confirm.text.toString()

    private fun isSomeFieldEmpty() =
            user_nombre.text.toString() == "" || user_apellido.text.toString() == "" || user_email.text.toString() == "" || user_password_signIn.text.toString() == "" || user_password_signIn_confirm.text.toString() == ""

    private fun automaticLogIn(email: String, password: String) {
        val appPreferences = MyPreferences(this)
        val volleyQueue = Volley.newRequestQueue(this)
        val url = URL_BASE + "api.login/"

        Log.i(Login.LOG_TAG, email)
        Log.i(Login.LOG_TAG, password)

        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)

        Log.i(Login.LOG_TAG, "JsonObject es: $jsonObject")
        val jsonObjectRequest = JsonObjectRequest(url, jsonObject,
                Response.Listener { response ->
                    try {

                        val gsonBuilder = GsonBuilder().serializeNulls()
                        gsonBuilder.registerTypeAdapter(UserShare::class.java, UserShareDeserealizer())
                        val gson = gsonBuilder.create()

                        val userShareJsonParsed: UserShare = gson.fromJson(response.toString(), UserShare::class.java)
                        if (!userShareJsonParsed.sucess!!) throw Exception("Not Logued In");

                        Log.i(Login.LOG_TAG, "userShareJsonParsed.email es:" + userShareJsonParsed.email)

                        //Se guardan valores del Email en Share Preferences
                        appPreferences.setUserEmail(userShareJsonParsed.email!!)
                        val userEmail = appPreferences.getUserEmail()
                        Log.i(Login.LOG_TAG, "El email es: $userEmail")


                        //Se guardan valores del Email en Share Preferences
                        appPreferences.setUserId(userShareJsonParsed.userId!!)
                        val userID = appPreferences.getUserId()
                        Log.i(Login.LOG_TAG, "El userID es: $userID")


                        startActivity(Intent(this, MenuActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(Login.LOG_TAG, "Error al Login.")
                        Toast.makeText(this, R.string.ERROR_STANDARD, Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, Login::class.java))
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Log.e(Login.LOG_TAG, "Error al Login.")
                    Toast.makeText(this, R.string.ERROR_STANDARD, Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Login::class.java))
                }
        )

        // Add the request to the RequestQueue.
        volleyQueue.add(jsonObjectRequest)
    }


    private fun emailIsInvalid(emailString: CharSequence): Boolean {
        return if (TextUtils.isEmpty(emailString)) {
            true
        } else {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()
        }
    }
}

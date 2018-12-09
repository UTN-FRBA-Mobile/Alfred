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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btn_sing_in_ingresar.setOnClickListener {
             startActivity(Intent(this, Login::class.java))
        }

        btn_sing_in_registrarse.setOnClickListener {
            if( !isValidEmail(user_password_signIn.text) ) {
                Toast.makeText(this, "El email no es valido, por favor reviselo", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(user_password_signIn.text.toString() != user_password_signIn_confirm.text.toString()){
                Toast.makeText(this, "No coinciden las contraseñas", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            /**
             * Llamada POST que envia un JSONObject y devuelve un JSONobject
            Log.i(Login.LOG_TAG, user_nombre.text.toString())
            Log.i(Login.LOG_TAG, user_password_signIn.text.toString())
            Log.i(Login.LOG_TAG, user_email.text.toString())
            Log.i(Login.LOG_TAG, "jsonObjectRequestPost")
             */

            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)

            val url = "http://178.128.229.73:3300/methods/api.RegisterUser"

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

    private fun automaticLogIn(email: String, password: String) {
        val mypreference = MyPreferences(this)
        val queue = Volley.newRequestQueue(this)
        val url = "http://178.128.229.73:3300/methods/api.login/"

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
                        // Log.i(LOG_TAG, "userShareJsonParsed.userId es:" +  userShareJsonParsed.userId)

                        //Se guardan valores del Email en Share Preferen
                        mypreference.setUserEmail(userShareJsonParsed.email!!)
                        val userEmail = mypreference.getUserEmail()
                        Log.i(Login.LOG_TAG, "El email es: $userEmail")

                        Toast.makeText(this, "Bienvenido!", Toast.LENGTH_LONG).show()


                        //Se guardan valores del Email en Share Preferen
                        mypreference.setUserId(userShareJsonParsed.userId!!)
                        val userID = mypreference.getUserId()
                        Log.i(Login.LOG_TAG, "El userID es: $userID")


                        startActivity(Intent(this, MenuActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(Login.LOG_TAG, "Error al Login.")
                        var errorString = "Hubo un error de conexión, por favor Ingrese de forma manual"
                        Toast.makeText(this, errorString, Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, Login::class.java))
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Log.e(Login.LOG_TAG, "Error al Login.")
                    var errorString = "Hubo un error de conexión, por favor Ingrese de forma manual"
                    Toast.makeText(this, errorString, Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Login::class.java))
                }
        )

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    //FIX this validation
    private fun isValidEmail(emailString: CharSequence): Boolean {
// I seriously don't understand why this doesn't work
//        return if (TextUtils.isEmpty(target)) {
//            false
//        } else {
//            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
//        }
        return "@" in emailString && "." in emailString
    }
}

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

        val mypreference = MyPreferences(this)

        try {
            //Si hay un logueo previo lanzo directamente el MenuActivity
            mypreference.getUserId()
            startActivity(Intent(this, MenuActivity::class.java))

        }catch (e: Exception){
            //En caso de no poder recuperar un user, muestro pantalla de logueo
            Log.i("LOGIN", "NO USER LOGUED")

            setContentView(R.layout.activity_login)
            btn_login_ingresar.setOnClickListener {
                execLogin(mypreference)

            }

            btn_login_registrarse.setOnClickListener {
                startActivity(Intent(this, SignIn::class.java))
            }
        }
  /*
        //CRASHEA
        if (!mypreference.getUserId().isNullOrEmpty()){
            Log.i("LOGIN", "LOGUED")

            Log.i("LOGIN", mypreference.getUserId())
            Log.i("LOGIN",mypreference.getUserEmail())
            startActivity(Intent(this, MenuActivity::class.java))

        }else{
            Log.i("LOGIN", "NO USER LOGUED")

            setContentView(R.layout.activity_login)
            btn_login_ingresar.setOnClickListener {
                execLogin(mypreference)

            }

            btn_login_registrarse.setOnClickListener {
                startActivity(Intent(this, SignIn::class.java))
            }
        }
*/

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

            if(user_name.text.toString() == "" || user_password.text.toString() == ""){
                Toast.makeText(this, R.string.ERROR_LOGIN_EMPTY, Toast.LENGTH_LONG).show()
                return
            }

            Log.i(LOG_TAG, user_name.text.toString())
            Log.i(LOG_TAG, user_password.text.toString())

            val jsonObject = JSONObject()
            jsonObject.put("email", user_name.text.toString())
            jsonObject.put("password", user_password.text.toString())

            Log.i(LOG_TAG, "JsonObject es: $jsonObject")
            // Request a JSONObject response from the provided URL.
            val jsonObjectRequest = JsonObjectRequest(url, jsonObject,
                    Response.Listener { response ->
                        try {
                            val gsonBuilder = GsonBuilder().serializeNulls()
                            gsonBuilder.registerTypeAdapter(UserShare::class.java, UserShareDeserealizer())
                            val gson = gsonBuilder.create()

                            val userShareJsonParsed: UserShare = gson.fromJson(response.toString(), UserShare::class.java)
                            if (!userShareJsonParsed.sucess!!) throw Exception("Not Logued In");
                            // use userShareJsonParsed.email and userShareJsonParsed.userId with shared preferences
                            Log.i(LOG_TAG, "userShareJsonParsed.email es:" + userShareJsonParsed.email)

                            //Se guardan valores del Email en Share Preferen
                            mypreference.setUserEmail(userShareJsonParsed.email!!)
                            val userEmail = mypreference.getUserEmail()
                            Log.i(LOG_TAG, "El email es: $userEmail")


                            //Se guardan valores del Email en Share Preferen
                            mypreference.setUserId(userShareJsonParsed.userId!!)
                            val userID = mypreference.getUserId()
                            Log.i(LOG_TAG, "El userID es: $userID")

                            startActivity(Intent(this, MenuActivity::class.java))
                            this.finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e(LOG_TAG, "Error al Login.")
                            Toast.makeText(this, R.string.ERROR_LOGIN, Toast.LENGTH_LONG).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        error.printStackTrace()
                        Log.e(LOG_TAG, "Error al Login.")
                        Toast.makeText(this, R.string.ERROR_LOGIN, Toast.LENGTH_LONG).show()
                    }
            )

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(LOG_TAG, "Error al Login.")
            Toast.makeText(this, R.string.ERROR_LOGIN, Toast.LENGTH_LONG).show()
        }
    }
}

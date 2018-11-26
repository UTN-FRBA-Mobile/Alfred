package com.botigocontigo.alfred

import android.content.Context
import android.content.SharedPreferences
import android.R.id.edit





class MyPreferences(context: Context) {

    val PREFERENCE_NAME = "SharecPreferenceExample"
    val PREFERENCE_USER_ID = "UserId"
    val PREFERENCE_USER_EMAIL = "UserEmail"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getUserId(): String {
        return preference.getString(PREFERENCE_USER_ID, null)
    }

    fun setUserId( cadena: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_USER_ID,cadena)
        editor.apply()
    }

    fun getUserEmail(): String {
        return preference.getString(PREFERENCE_USER_EMAIL, null)
    }

    fun setUserEmail( cadena: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_USER_EMAIL,cadena)
        editor.apply()
    }

    fun getName(): String {
        return preference.getString(PREFERENCE_NAME, null)
    }

    fun setName( cadena: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_NAME, cadena)
        editor.apply()
    }

}
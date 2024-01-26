package com.example.core.data.source.local.shared_preferences

import android.content.SharedPreferences

class ApiPreference(private val sharedPreferences: SharedPreferences) {

    fun setTokenApi(token: String){
        sharedPreferences.edit()
            .putString(TOKEN_API, token)
            .apply()
    }

    fun getTokeApi():String? = sharedPreferences.getString(TOKEN_API, null)

    companion object {
        private const val TOKEN_API = "token_api"
    }
}
package com.chaitanya.openaiassignment.data.local

import android.content.SharedPreferences
import javax.inject.Inject

class TokenManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
    }
    init {
        val initialToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
        saveAccessToken(initialToken)
    }

    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }
}

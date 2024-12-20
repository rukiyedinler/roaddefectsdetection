package com.rukiyedinler.roaddefectsdetection.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.google.gson.Gson

data class TokenPayload(val role: String)

class AuthToken private constructor(val context: Context) {
    companion object {

        private const val TOKEN = "TOKEN"
        private const val TOKEN_VALUE = "TOKEN_VALUE"

        @Volatile
        private var instance: AuthToken? = null

        fun getInstance(context: Context): AuthToken = instance ?: synchronized(this) {
            AuthToken(context).apply { instance = this }
        }

        fun getRole(context: Context) {

        }

    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
    var token: String? = null
        set(value) = sharedPreferences.edit().putString(TOKEN_VALUE, value).apply()
            .also { field = value }
        get() = field ?: sharedPreferences.getString(TOKEN_VALUE, null)

    var role: String? = null

        get() {
            token?.let {
                val elements = it.split('.')
                if (elements.size == 3) {
                    val payload = Base64.decode(elements[1], Base64.DEFAULT).decodeToString()
                    return Gson().fromJson(payload, TokenPayload::class.java).role
                }
            }
            return null
        }
}
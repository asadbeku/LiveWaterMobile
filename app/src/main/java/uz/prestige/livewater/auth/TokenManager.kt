package uz.prestige.livewater.auth

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "MyPrefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_EXPIRES_AT = "expires_at"
    private const val KEY_ROLE = "role"
    private const val PROFILE_TYE = "profile_type"

    fun saveToken(context: Context, token: String, expiresIn: Long, role: String) {
        val currentTimeMillis = System.currentTimeMillis()
        val expiresAt = currentTimeMillis + expiresIn * 1000 // expiresIn is in seconds

        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_TOKEN, token)
        editor.putLong(KEY_EXPIRES_AT, expiresAt)
        editor.putString(KEY_ROLE, role) // Save the role
        editor.apply()
    }

    fun getToken(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun isTokenValid(context: Context): Boolean {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val expiresAt = sharedPreferences.getLong(KEY_EXPIRES_AT, 0)
        return expiresAt > System.currentTimeMillis()
    }

    fun clearToken(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_TOKEN)
        editor.remove(KEY_EXPIRES_AT)
        editor.apply()
    }

    fun getRole(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_ROLE, null) // Get the role
    }

    fun getProfileType(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PROFILE_TYE, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PROFILE_TYE, null)
    }

    fun setProfileType(context: Context, profileType: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PROFILE_TYE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(PROFILE_TYE, profileType)
        editor.apply()
    }
}
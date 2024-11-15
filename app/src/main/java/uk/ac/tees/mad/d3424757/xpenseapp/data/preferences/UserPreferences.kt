package uk.ac.tees.mad.d3424757.xpenseapp.data.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    fun isUserRegistered(): Boolean {
        return sharedPreferences.getBoolean("is_user_registered", false)
    }

    fun setUserRegistered(isRegistered: Boolean) {
        sharedPreferences.edit().putBoolean("is_user_registered", isRegistered).apply()
    }

}


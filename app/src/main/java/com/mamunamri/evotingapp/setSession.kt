package com.mamunamri.evotingapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat.startActivity
import kotlin.jvm.internal.Intrinsics

class setSession(context: Context) {
    companion object{
        val KEY_KELAS = "key_kelas"
        val KEY_LEVEL = "key_level"
        val KEY_LOGIN = "key_login"
        val KEY_NISN = "key_nisn"
        val TAG = "login"
    }

    private val LOGIN_STATUS = "login_status"
    private val ROLE = "role"


    var sharedPreference : SharedPreferences = context.getSharedPreferences(ROLE,Context.MODE_PRIVATE)
    var editor : SharedPreferences.Editor = sharedPreference.edit()



    fun setUserString(KEY_NAME: String?, value: String?) {
        Intrinsics.checkNotNullParameter(KEY_NAME, "KEY_NAME")
        Intrinsics.checkNotNullParameter(value, "value")
        editor!!.putString(KEY_NAME, value)
        editor!!.commit()
    }

    fun getUserString(KEY_NAME: String?): String? {
        Intrinsics.checkNotNullParameter(KEY_NAME, "KEY_NAME")
        return sharedPreference!!.getString(KEY_NAME, "")
    }

    fun setUserBoolean(KEY_NAME: String?, value: Boolean) {
        Intrinsics.checkNotNullParameter(KEY_NAME, "KEY_NAME")
        editor!!.putBoolean(KEY_NAME, value)
        editor!!.commit()
    }

    fun getUserBoolean(KEY_NAME: String?): Boolean {
        Intrinsics.checkNotNullParameter(KEY_NAME, "KEY_NAME")
        return sharedPreference!!.getBoolean(KEY_NAME, false)
    }

    fun clearePref() {
        editor!!.clear()
        editor!!.apply()
        editor!!.commit()
    }
}

package com.doubleapaper.photostamp.aatracking.manager

import android.content.Context
import com.doubleapaper.photostamp.aatracking.R

class PrefManage {
    private var instance: PrefManage? = null
    private val mContext: Context
    init {
        mContext = Contextor.getInstance().context!!
    }
    companion object {
        private var instance: PrefManage? = null

        fun getInstance(): PrefManage {
            if (instance == null)
                instance = PrefManage()
            return instance!!
        }
    }
    object Key {
        val TMS = instance!!.mContext.getString(R.string.app_name)
        val UserName = "username"
        val IMEI = "imei"
    }

    fun getIMEI(): String? {
        return mContext.getSharedPreferences(Key.TMS, Context.MODE_PRIVATE).getString(Key.IMEI, "")
    }
    fun getUserName(): String? {
        return mContext.getSharedPreferences(Key.TMS, Context.MODE_PRIVATE).getString(Key.UserName, "")
    }
    fun setIMEI(IMEI: String) {
        mContext.getSharedPreferences(Key.TMS, Context.MODE_PRIVATE).edit().putString(Key.IMEI, IMEI)
            .apply()
    }
    fun setUserName(UserName: String) {
        mContext.getSharedPreferences(Key.TMS, Context.MODE_PRIVATE).edit().putString(Key.UserName, UserName)
            .apply()
    }
}
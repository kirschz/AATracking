package com.doubleapaper.photostamp.aatracking.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.content.DialogInterface
import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.widget.AppCompatCheckBox
import cn.pedant.SweetAlert.SweetAlertDialog




object Utils {
    fun startPowerSaverIntent(context: Context) {

        val settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE)
        val skipMessage = settings.getBoolean("skipProtectedAppCheck", false)
        if (!skipMessage) {
            val editor = settings.edit()
            var foundCorrectIntent = false

            for (intent in Constants.POWERMANAGER_INTENTS) {
                if (isCallable(context, intent)) {
                    foundCorrectIntent = true
                    val dontShowAgain = AppCompatCheckBox(context)
                    dontShowAgain.text = "Do not show again"
                    dontShowAgain.setOnCheckedChangeListener { buttonView, isChecked ->
                        editor.putBoolean("skipProtectedAppCheck", isChecked)
                        editor.apply()
                    }

                    AlertDialog.Builder(context)
                        .setTitle(Build.MANUFACTURER + " Protected Apps")
                        .setMessage(
                            String.format(
                                "%s requires to be enabled in 'Protected Apps' to function properly.%n",
                                context.getString(com.doubleapaper.photostamp.aatracking.R.string.app_name)
                            )
                        )
                        .setView(dontShowAgain)
                        .setPositiveButton("Go to settings",
                            DialogInterface.OnClickListener { dialog, which -> context.startActivity(intent) })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()
                    break
                }
            }
            if (!foundCorrectIntent) {
                editor.putBoolean("skipProtectedAppCheck", true)
                editor.apply()
            }
        }
    }
    private fun isCallable(context: Context, intent: Intent): Boolean {
        val list = context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return list.size > 0
    }

}
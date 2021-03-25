package com.droid.offlineStorage.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Akhtar
 */
object CommonUtils {
    @SuppressLint("SimpleDateFormat")
    fun getDateAndTime(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedDate = df.format(c.time)
        return formattedDate
    }

    infix fun Context.appToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Context.isNetworkConnected(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}
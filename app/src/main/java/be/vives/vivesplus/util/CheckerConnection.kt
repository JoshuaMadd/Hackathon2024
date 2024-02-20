/**
 * Created by Cantaert Jordy
 * 10 feb 2020
 *
 * Last edited by Cantaert Jordy
 * 10 feb 2020
 */

package be.vives.vivesplus.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import be.vives.vivesplus.R

class CheckerConnection(c: Context) {
    val context = c

    fun hasInternetConnection(): Boolean {
        return if(isNetworkAvailable(context)) {
            true
        } else {
            noNetworkToast()
            false
        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    private fun noNetworkToast() {
        Toast.makeText(context, context.getString(R.string.geen_internet), Toast.LENGTH_LONG).show()
    }
}

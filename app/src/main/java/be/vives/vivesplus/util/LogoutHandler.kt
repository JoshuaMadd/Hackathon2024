package be.vives.vivesplus.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import be.vives.vivesplus.MainActivity
import be.vives.vivesplus.R


class LogoutHandler {
    companion object {
        fun showLogoutAlert(context: Context, activity: Activity) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(context.getString(R.string.somethingWentWrongLogout))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.logout)) { _, _ ->
                    logout(context, activity)
                }
            val alert = builder.create()
            alert.show()
        }


        fun showLogoutAlertMenu(context: Context, activity: Activity) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(context.getString(R.string.logout_message))
                .setCancelable(true)
                .setPositiveButton(context.getString(R.string.logout)) { _, _ ->
                    logout(context, activity)
                }
                .setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        private fun logout(context: Context, activity: Activity) {
            val a = activity as MainActivity
            a.showNavigationAndStartScreen(R.id.loginFragment)
        }

    }
}
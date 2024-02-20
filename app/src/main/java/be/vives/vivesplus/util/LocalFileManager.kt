/**
 * Created by Cantaert Jordy
 * 10 feb 2020
 *
 * Last edited by Cantaert Jordy
 * 10 feb 2020
 */

package be.vives.vivesplus.util

import android.content.Context
import be.vives.vivesplus.R
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class LocalFileManager(val context: Context, val filename: String) {

    fun saveStringToFile(result: String) {
        try {
            val fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            fileOutputStream.write(result.toByteArray())
            fileOutputStream.close()
        } catch (ex: Exception) {
            println(ex.toString())
        }
    }

    fun getStringFromFile(): String {
        var sb: StringBuilder? = null
        try {
            val inputStreamReader = InputStreamReader(context.openFileInput(filename))
            val bufferReader = BufferedReader(inputStreamReader)
            sb = StringBuilder()
            var nextLine = ""
            while(bufferReader.readLine().also { nextLine = it } != null) {
                sb.append(nextLine)
            }
        } catch (ex: Exception) {
            println(ex.toString())
        }

        return sb?.toString() ?: ""
    }

    fun clearMyFiles() {
        PreferencesManager().writeStringToPreferences(context, context.getString(R.string.pref_jwt), "")
        PreferencesManager().writeStringToPreferences(context, context.getString(R.string.pref_last_date_message_viewed), "")
        PreferencesManager().writeIntToPreferences(context, context.getString(R.string.lastselectedcampusrestaurant), -1)
        PreferencesManager().writeIntToPreferences(context, context.getString(R.string.lastselectedcampusbus), -1)
        PreferencesManager().writeIntToPreferences(context, context.getString(R.string.lastselectedcampustrain), -1)
        PreferencesManager().writeStringToPreferences(context, context.getString(R.string.selected_bus_id), "As5d4fsd4f")
        PreferencesManager().writeStringToPreferences(context, context.getString(R.string.pref_home_station), "")
        PreferencesManager().writeStringToPreferences(context, context.getString(R.string.pref_campus_station), "")
        PreferencesManager().writeStringToPreferences(context, context.getString(R.string.pref_role), "")
        val files: Array<File>? = context.filesDir.listFiles()
        if (files != null) for (file in files) {
            file.delete()
        }
    }
}
package be.vives.vivesplus.util

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.enum.ProfileRole

class CheckerRole (val context: Context) {

    fun checkRole () : ProfileRole? {
        val role: String? = PreferencesManager().getStringFromPreferences(context, context.getString(R.string.pref_role))
        var profileRole : ProfileRole? = null
        if (!role.isNullOrEmpty()) {
            if(role == "STUDENT") {
                profileRole = ProfileRole.STUDENT
            }
            else if(role == "MEMBER") {
                profileRole = ProfileRole.MEMBER
            }
        }
        return profileRole
    }
}
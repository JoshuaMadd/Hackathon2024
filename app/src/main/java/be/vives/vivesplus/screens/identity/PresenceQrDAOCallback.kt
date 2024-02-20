package be.vives.vivesplus.screens.identity

import be.vives.vivesplus.model.CodeQR
import com.android.volley.VolleyError

interface PresenceQrDAOCallback {
    fun dataLoaded(ticket: CodeQR)
    fun setErrorPost(error: Int){ }
}

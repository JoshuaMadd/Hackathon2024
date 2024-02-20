package be.vives.vivesplus.util

interface WebservicePostCallback {
    fun postSuccesfull()
    fun postSuccesfull(response: String)
    fun setErrorPost(error: Int){}
}
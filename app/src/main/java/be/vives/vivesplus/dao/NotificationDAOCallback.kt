package be.vives.vivesplus.dao

interface NotificationDAOCallback {
    fun postOrPutSuccesful()
    fun setErrorPost(error: Int) {}
}
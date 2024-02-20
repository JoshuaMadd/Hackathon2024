package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Message

interface MessagesDAOCallback {
    fun dataLoaded(messages: ArrayList<Message>)
}
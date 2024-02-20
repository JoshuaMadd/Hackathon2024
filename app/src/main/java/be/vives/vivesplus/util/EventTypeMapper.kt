package be.vives.vivesplus.util

import be.vives.vivesplus.enum.EventType

class EventTypeMapper {
    fun mapJsonToEventType(jsonValue : String) : EventType {
        return when(jsonValue.uppercase()) {
            "COURSE" -> {
                EventType.COURSE
            }
            "CANCELLED" -> {
                EventType.CANCELLED
            }
            "ACADEMIC" -> {
                EventType.ACADEMIC
            }
            else -> {
                EventType.ACADEMIC
            }
        }
    }
}
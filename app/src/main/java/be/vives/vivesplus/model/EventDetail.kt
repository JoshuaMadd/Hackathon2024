package be.vives.vivesplus.model

import be.vives.vivesplus.enum.EventType

abstract class EventDetail(
    open var id: String,
    open var startDateTime: String,
    open var endDateTime: String,
    open var description : String,
    open var allDay : Boolean,
    open var locations : ArrayList<Location>,
    open var type : EventType
) {
}

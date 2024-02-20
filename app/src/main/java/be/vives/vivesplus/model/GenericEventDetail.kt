package be.vives.vivesplus.model

import be.vives.vivesplus.enum.EventType

data class GenericEventDetail(
    override var id: String,
    override var startDateTime: String,
    override var endDateTime: String,
    override var description : String,
    override var allDay : Boolean,
    override var locations : ArrayList<Location>,
    override var type : EventType
) : EventDetail(id, startDateTime, endDateTime, description, allDay, locations, type) {
}
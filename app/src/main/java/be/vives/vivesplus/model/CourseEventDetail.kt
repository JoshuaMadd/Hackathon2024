package be.vives.vivesplus.model

import be.vives.vivesplus.enum.EventType

data class CourseEventDetail(
    override var id: String,
    override var startDateTime: String,
    override var endDateTime: String,
    override var description: String,
    override var allDay: Boolean,
    override var locations: ArrayList<Location>,
    var teachers: ArrayList<Teacher>?,
    override var type: EventType,
    val ectsCode : String,
    val groupInfo : String
) : EventDetail(id, startDateTime, endDateTime, description, allDay, locations, type)  {
}

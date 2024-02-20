package be.vives.vivesplus.model

data class Student(
    val kulNumber : String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val study: Study?,
    val campus: Campus?,
    val fieldOfStudy: FieldOfStudy?,
    val transportOptions: ArrayList<TransportOption>,
    val optInJob : Boolean
)

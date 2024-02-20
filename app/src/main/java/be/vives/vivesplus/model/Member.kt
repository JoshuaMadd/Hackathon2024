package be.vives.vivesplus.model

data class Member(
    val firstName: String,
    val lastName: String,
    val photoUrl: String?,
    val kulNumber: String,
    val phoneNumber: String,
    val functieOmschrijving: String,
    val email: String,
    val campusses : ArrayList<Campus>,
    val fieldOfStudies: ArrayList<FieldOfStudy>,
    val studies: ArrayList<Study>,
    val transportOptions: ArrayList<TransportOption>,
    val bachelor : Boolean,
    val graduaat : Boolean
    ){
    override fun toString(): String {
        return "$lastName $firstName"
    }

}
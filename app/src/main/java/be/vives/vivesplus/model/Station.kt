package be.vives.vivesplus.model

data class Station(
    val name: String,
    val id: Int,
    val campusId: Any?
){
    override fun toString(): String {
        return name
    }
}
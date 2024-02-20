package be.vives.vivesplus.model

data class FieldOfStudy(
    val id: Int,
    val name: String
)  {
    override fun toString(): String {
        return name
    }
}
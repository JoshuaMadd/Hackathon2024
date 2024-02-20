package be.vives.vivesplus.model

data class Campus(
    val id: Int,
    val name: String
)
{
    override fun toString(): String {
        return name
    }
}
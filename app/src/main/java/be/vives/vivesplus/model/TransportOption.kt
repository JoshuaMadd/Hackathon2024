package be.vives.vivesplus.model

data class TransportOption(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}
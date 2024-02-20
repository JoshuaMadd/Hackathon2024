package be.vives.vivesplus.model

data class Bus(
    val halteId: String,
    val halteNaam: String,
    val adres: String,
    val infoUrl: String
){
    override fun toString(): String {
        return halteNaam
    }
}
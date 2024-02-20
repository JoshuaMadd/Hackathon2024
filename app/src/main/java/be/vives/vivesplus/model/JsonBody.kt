package be.vives.vivesplus.model

data class JsonBody(
    val name: String,
    val valueInt: Int?,
    val valueString: String?,
    val valueArray: MutableList<Int>?,
    val valueBoolean: Boolean?
)
package ru.rescqd.jetschedule.data.container

data class GenerateDataContainer(
    val subject: String,
    val audience: Audience,
    val pairOrder: Int,
    val teacher: String,
    val group: String
){
    data class Audience(
        val name: String,
        val floor: Int = if (name[1].isDigit()) name[1].digitToInt()  else 1,
        val corpus: Int = if (name[0].isDigit()) name[0].digitToInt()  else 1
    )
}

package ru.rescqd.jetschedule.data.remote.network.entity

data class GenerateDataRequestEntity(
    val data: String,
    val type: String,
    val id: String
) {
    override fun toString(): String {
       return "type=$type&data=$data&id=$id"
    }
    companion object{
        const val GROUP = "1"
        const val TEACHER = "2"
    }
}
package ru.rescqd.jetschedule.data.remote.network.entity

data class BackParamRequestEntity(
    val data: String,
    val type_id: String
){
    override fun toString(): String {
        return "type_id=${type_id}&data=$data"
    }
    companion object{
        const val TEACHER = "2"
        const val GROUP = "1"
    }
}
package ru.rescqd.jetschedule.data.remote.network.entity

data class CheckFileRequestEntity(
    val data: String
){
    override fun toString(): String {
        return "data=$data"
    }
    companion object{
            const val OK = "ok"
            const val ERR = "err"
    }
}


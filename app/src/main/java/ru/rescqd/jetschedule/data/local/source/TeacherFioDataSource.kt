package ru.rescqd.jetschedule.data.local.source

import ru.rescqd.jetschedule.data.container.TeacherFIOContainer

//TODO("Hardcode")
class TeacherFioDataSource {
    suspend fun getTeacherFIOByDefaultValue(commonValue: String): TeacherFIOContainer {
        return when(commonValue){
            "Таспаева М.Г." -> TeacherFIOContainer(family = "Таспаева",
                name = "Мира",
                path = "Гайзулловна")
            else ->TeacherFIOContainer("", "", "")
        }
    }
}
package ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model

import ru.rescqd.jetschedule.ui.components.AutoCompleteEntity

data class TeacherModel(
    val name: String,
) : AutoCompleteEntity {
    override fun filter(query: String): Boolean =
        name.startsWith(query, ignoreCase = true)
}

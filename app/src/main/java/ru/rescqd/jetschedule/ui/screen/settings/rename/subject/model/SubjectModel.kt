package ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model

import ru.rescqd.jetschedule.ui.components.AutoCompleteEntity

data class SubjectModel(
    val uid: Long,
    val name: String,
    val displayName: String
) : AutoCompleteEntity {
    override fun filter(query: String): Boolean =
        name.startsWith(query, ignoreCase = true)
}

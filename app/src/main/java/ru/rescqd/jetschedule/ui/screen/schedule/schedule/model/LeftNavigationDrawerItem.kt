package ru.rescqd.jetschedule.ui.screen.schedule.schedule.model

import androidx.annotation.Keep
import kotlinx.serialization.Contextual

@Keep
@kotlinx.serialization.Serializable
data class LeftNavigationDrawerItem(
    val displayName: String,
    val itemType: LeftNavigationDrawerItemType,
    @Contextual val itemPayload: LeftNavigationDrawerItemPayload,
    val deletable: Boolean = true,
)

enum class LeftNavigationDrawerItemType {
    GROUP, TEACHER, CUSTOM_SELECT
}

@Keep
@kotlinx.serialization.Serializable
sealed class LeftNavigationDrawerItemPayload {
    @Keep
    @kotlinx.serialization.Serializable
    data class GroupPayload(
        val groupName: String,
    ) : LeftNavigationDrawerItemPayload()

    @Keep
    @kotlinx.serialization.Serializable
    data class TeacherPayload(
        val teacherName: String,
    ) : LeftNavigationDrawerItemPayload()

    @Keep
    @kotlinx.serialization.Serializable
    object CustomSelectPayload : LeftNavigationDrawerItemPayload()
}
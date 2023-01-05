package ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model

import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemPayload
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemType


sealed class AddDrawerViewState {
    object Loading : AddDrawerViewState()
    object DisplaySelectType : AddDrawerViewState()

    data class DisplayGroupSelect(
        val label: String,
        val groups: List<GroupModel>,
        val selectedGroup: GroupModel? = null
    ) : AddDrawerViewState()

    data class DisplayTeacherSelect(
        val label: String,
        val teachers: List<TeacherModel>,
        val selectedTeacher: TeacherModel? = null
    ) : AddDrawerViewState()

    data class DialogConfirmExit(
        val label: String,
        val type: LeftNavigationDrawerItemType,
        val payload: LeftNavigationDrawerItemPayload,
    ): AddDrawerViewState()
}

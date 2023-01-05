package ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model

import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemPayload
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemType


sealed class AddDrawerEvent {
    data class EnterScreen(
        val addItemInfo: (String, LeftNavigationDrawerItemType, LeftNavigationDrawerItemPayload) -> Unit
    ) : AddDrawerEvent()
    data class GroupSelected(val group: GroupModel): AddDrawerEvent()
    data class TeacherSelected(val teacher: TeacherModel): AddDrawerEvent()
    data class ItemTypeSelected(val itemType: AddDrawerItemType) : AddDrawerEvent()
    object ConfirmClicked: AddDrawerEvent()
}

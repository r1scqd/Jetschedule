package ru.rescqd.jetschedule.ui.screen.schedule.schedule.model

sealed class ScheduleEvent {
    object EnterScreen : ScheduleEvent()
    data class LeftNavigationDrawerItemChanged(val item: LeftNavigationDrawerItem) :
        ScheduleEvent()

    data class LeftNavigationDrawerItemAdd(val newItem: LeftNavigationDrawerItem) :
        ScheduleEvent()

    data class LeftNavigationDrawerItemDelete(val deleteItem: LeftNavigationDrawerItem) :
        ScheduleEvent()
}

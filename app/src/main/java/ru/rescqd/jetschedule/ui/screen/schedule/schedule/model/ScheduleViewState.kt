package ru.rescqd.jetschedule.ui.screen.schedule.schedule.model

sealed class ScheduleViewState {
    object Loading: ScheduleViewState()
    data class Display(
        val items: Collection<LeftNavigationDrawerItem>,
        val currentItem: LeftNavigationDrawerItem
    ): ScheduleViewState()
}
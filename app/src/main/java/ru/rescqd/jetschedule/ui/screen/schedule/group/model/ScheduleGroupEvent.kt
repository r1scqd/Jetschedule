package ru.rescqd.jetschedule.ui.screen.schedule.group.model

import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemPayload
import java.time.LocalDate


sealed class ScheduleGroupEvent {
    data class EnterScreen(
        val payload: LeftNavigationDrawerItemPayload.GroupPayload,
    ) : ScheduleGroupEvent()
    object GroupTitleClicked : ScheduleGroupEvent()
    data class DateSelected(val date: LocalDate) : ScheduleGroupEvent()
    data class PairClicked(val scheduleUid: Long) : ScheduleGroupEvent()
    object BackPressed: ScheduleGroupEvent()
}

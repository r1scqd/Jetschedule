package ru.rescqd.jetschedule.ui.screen.schedule.teacher.model

import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemPayload
import java.time.LocalDate


sealed class ScheduleTeacherEvent {
    data class EnterScreen(
        val payload: LeftNavigationDrawerItemPayload.TeacherPayload,
    ) : ScheduleTeacherEvent()
    object TeacherTitleClicked : ScheduleTeacherEvent()
    data class DateSelected(val date: LocalDate) : ScheduleTeacherEvent()
    data class PairClicked(val scheduleUid: Long) : ScheduleTeacherEvent()
}

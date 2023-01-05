package ru.rescqd.jetschedule.ui.screen.schedule.manager.model

import java.time.LocalDate

sealed class ScheduleManagerEvent{
    object EnterScreen: ScheduleManagerEvent()
    data class DateClicked(val date: LocalDate): ScheduleManagerEvent()
    data class ModeChanged(val mode: ScheduleManagerMode): ScheduleManagerEvent()
}


enum class ScheduleManagerMode{
    REMOVE, ADD
}
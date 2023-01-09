package ru.rescqd.jetschedule.ui.screen.schedule.manager.model

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

sealed class ScheduleManagerViewState {
    object Loading : ScheduleManagerViewState()
    data class Display(
        val selectDate: Flow<List<LocalDate>>,
        val mode: ScheduleManagerMode
    ) : ScheduleManagerViewState()

    data class Error(val cause: String) : ScheduleManagerViewState()
}
package ru.rescqd.jetschedule.ui.screen.schedule.manager.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import kotlinx.coroutines.flow.Flow
import ru.rescqd.jetschedule.ui.components.DefaultDay
import ru.rescqd.jetschedule.ui.components.DefaultMonthHeader
import ru.rescqd.jetschedule.ui.components.DefaultWeekHeader
import ru.rescqd.jetschedule.ui.screen.schedule.manager.model.ScheduleManagerViewState
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun ViewDisplay(
    modifier: Modifier,
    state: ScheduleManagerViewState.Display,
    onDateClick: (LocalDate) -> Unit,
) {
    Content(modifier = modifier, onDateClick = onDateClick, state.selectDate)
}

@Composable
private fun Content(
    modifier: Modifier,
    onDateClick: (LocalDate) -> Unit,
    colorizedDate: Flow<List<Long>>,
) {
    val colorizedDateState = colorizedDate.collectAsState(initial = emptyList())
    val calendarState = rememberSelectableCalendarState()
    Column(modifier = modifier
        .fillMaxSize()
    ) {
        SelectableCalendar(
            modifier = modifier,
            firstDayOfWeek = DayOfWeek.MONDAY,
            calendarState = calendarState,
            dayContent = { dayState ->
                DefaultDay(
                    dayState,
                    modifier,
                    onClick = onDateClick,
                    defaultDayColor =
                    if (colorizedDateState.value.contains(dayState.date.toEpochDay())) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                )
            },
            monthHeader = { DefaultMonthHeader(monthState = it, modifier) },
            weekHeader = { DefaultWeekHeader(daysOfWeek = it, modifier) }
        )
    }
}
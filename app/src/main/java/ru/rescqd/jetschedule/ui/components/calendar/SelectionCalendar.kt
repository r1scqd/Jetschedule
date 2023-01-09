package ru.rescqd.jetschedule.ui.components.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Composable
fun SelectionCalendar(
    modifier: Modifier = Modifier,
    selectionDateFlow: Flow<List<LocalDate>>,
    onClick: (LocalDate) -> Unit,
    adjacentMonths: Long = 500,
    isWeekMode: Boolean = !false
) {
    val selections by selectionDateFlow.collectAsState(initial = emptyList())
    SelectionCalendarContent(
        modifier, selections, onClick, adjacentMonths, isWeekMode = isWeekMode
    )
}

@Composable
fun WeekCalendar(
    modifier: Modifier = Modifier,
    selectionDateFlow: Flow<List<LocalDate>>,
    selectionDate: LocalDate,
    onClick: (LocalDate) -> Unit,
    adjacentMonths: Long = 500,
) {
    val selections by selectionDateFlow.collectAsState(initial = emptyList())
    WeekCalendarContent(
        modifier, selections, onClick, adjacentMonths, selectionDate
    )
}


@Stable
@Composable
private fun WeekCalendarContent(
    modifier: Modifier,
    selections: List<LocalDate>,
    onClick: (LocalDate) -> Unit,
    adjacentMonths: Long,
    selected: LocalDate
) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(adjacentMonths) }
    val endDate = remember { currentDate.plusDays(adjacentMonths) }
    val daysOfWeek = remember { daysOfWeek() }
    Column(modifier = modifier.fillMaxWidth()) {
        val state = rememberWeekCalendarState(
            startDate = startDate,
            endDate = endDate,
            firstVisibleWeekDate = currentDate,
        )
        val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)
        Text(
            text = getWeekPageTitle(visibleWeek),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge
        )
        CalendarHeader(daysOfWeek = daysOfWeek)

        com.kizitonwose.calendar.compose.WeekCalendar(
            state = state,
            dayContent = { day ->
                Day(
                    day.date,
                    day.date == selected,
                    isSelectable = selections.contains(day.date),
                    isToday = day.date == currentDate,
                    onClick = onClick
                )
            }
        )
    }

}
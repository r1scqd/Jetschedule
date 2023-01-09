package ru.rescqd.jetschedule.ui.screen.schedule.manager.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.calendar.SelectionCalendar
import ru.rescqd.jetschedule.ui.components.calendar.clickable
import ru.rescqd.jetschedule.ui.screen.schedule.manager.model.ScheduleManagerMode
import ru.rescqd.jetschedule.ui.screen.schedule.manager.model.ScheduleManagerViewState
import java.time.LocalDate

@Composable
fun ViewDisplay(
    modifier: Modifier,
    state: ScheduleManagerViewState.Display,
    onDateClick: (LocalDate) -> Unit,
    modeChange: (ScheduleManagerMode) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Box(modifier = Modifier.fillMaxHeight(.7f)) {
            CalendarContent(modifier = modifier, onDateClick = onDateClick, state.selectDate)
        }
        ModeContent(
            currentMode = state.mode,
            modeChange = modeChange,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)
        )
    }
}

@Composable
private fun ModeContent(
    modifier: Modifier,
    currentMode: ScheduleManagerMode,
    modeChange: (ScheduleManagerMode) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = stringResource(id = R.string.schedule_manager_mode_select))
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(ScheduleManagerMode.values()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { modeChange.invoke(it) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = it == currentMode, onClick = { modeChange.invoke(it) })
                    Text(text = stringResource(id = it.displayName))
                }
            }
        }
    }
}

@Composable
private fun CalendarContent(
    modifier: Modifier,
    onDateClick: (LocalDate) -> Unit,
    selectionDateFlow: Flow<List<LocalDate>>
) {
    Column(modifier = Modifier) {
        SelectionCalendar(
            modifier = modifier,
            selectionDateFlow = selectionDateFlow,
            onClick = onDateClick,
            isWeekMode = false
        )
    }
}

package ru.rescqd.jetschedule.ui.components.calendar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                text = dayOfWeek.displayText(),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun CalendarTitle(
    isWeekMode: Boolean,
    monthState: CalendarState,
    weekState: WeekCalendarState,
) {
    val visibleMonth = rememberFirstVisibleMonthAfterScroll(monthState)
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(weekState)
    MonthAndWeekCalendarTitle(
        isWeekMode = isWeekMode,
        currentMonth = if (isWeekMode) visibleWeek.days.first().date.yearMonth else visibleMonth.yearMonth,
        monthState = monthState,
        weekState = weekState,
    )
}

@Composable
fun MonthAndWeekCalendarTitle(
    isWeekMode: Boolean,
    currentMonth: YearMonth,
    monthState: CalendarState,
    weekState: WeekCalendarState,
) {
    val coroutineScope = rememberCoroutineScope()
    SimpleCalendarTitle(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
        currentMonth = currentMonth,
        goToPrevious = {
            coroutineScope.launch {
                if (isWeekMode) {
                    val targetDate = weekState.firstVisibleWeek.days.first().date.minusDays(1)
                    weekState.animateScrollToWeek(targetDate)
                } else {
                    val targetMonth = monthState.firstVisibleMonth.yearMonth.previousMonth
                    monthState.animateScrollToMonth(targetMonth)
                }
            }
        },
        goToNext = {
            coroutineScope.launch {
                if (isWeekMode) {
                    val targetDate = weekState.firstVisibleWeek.days.last().date.plusDays(1)
                    weekState.animateScrollToWeek(targetDate)
                } else {
                    val targetMonth = monthState.firstVisibleMonth.yearMonth.nextMonth
                    monthState.animateScrollToMonth(targetMonth)
                }
            }
        },
    )
}


@Composable
internal fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier.testTag("Decrement"), onClick = goToPrevious
        ) {
            Image(
                imageVector = Icons.Default.KeyboardArrowLeft,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                contentDescription = "Previous",
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier,
                text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .lowercase().replaceFirstChar { it.titlecase() },
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = currentMonth.year.toString(), style = MaterialTheme.typography.titleMedium
            )
        }
        IconButton(
            modifier = modifier, onClick = goToNext
        ) {
            Image(
                imageVector = Icons.Default.KeyboardArrowRight,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                contentDescription = "Next",
            )
        }
    }
}


@Composable
internal fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("MonthHeader"),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                text = dayOfWeek.displayText(),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}


@Composable
internal fun Day(
    day: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit, isToday: Boolean = false,
    isSelectable: Boolean = true,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .testTag("MonthDay")
            .padding(5.dp)
            .border(
                if (isToday) 1.dp else (-1).dp,
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(
                    35
                )
            )
            .padding(2.dp)
            .clip(CircleShape)
            .clickable(
                enabled = isSelectable,
                showRipple = !isSelected,
                onClick = { onClick(day) },
            ),

        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.dayOfMonth.toString(),
            color = when {
                isSelected -> MaterialTheme.colorScheme.primary
                isSelectable -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.error
            },
            fontSize = 14.sp,
        )
    }
}


@Composable
internal fun SelectionCalendarContent(
    modifier: Modifier,
    selections: List<LocalDate>,
    onClick: (LocalDate) -> Unit,
    adjacentMonths: Long,
    isWeekMode: Boolean = false,
) {
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(adjacentMonths) }
    val endMonth = remember { currentMonth.plusMonths(adjacentMonths / 2) }
    val daysOfWeek = remember { daysOfWeek() }

    Column(modifier = modifier.fillMaxWidth()) {
        val monthState = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
        )
        val weekState = rememberWeekCalendarState(
            startDate = startMonth.atStartOfMonth(),
            endDate = endMonth.atEndOfMonth(),
            firstVisibleWeekDate = currentDate,
            firstDayOfWeek = daysOfWeek.first(),
        )

        val visibleMonth = rememberFirstMostVisibleMonth(monthState, viewportPercent = 90f)
        CalendarTitle(
            isWeekMode, monthState, weekState
        )
        if (isWeekMode) CalendarHeader(daysOfWeek = daysOfWeek)
        val monthCalendarAlpha by animateFloatAsState(if (isWeekMode) 0f else 1f)
        val weekCalendarAlpha by animateFloatAsState(if (isWeekMode) 1f else 0f)
        var weekCalendarSize by remember { mutableStateOf(DpSize.Zero) }
        val weeksInVisibleMonth = visibleMonth.weekDays.count()
        val monthCalendarHeight by animateDpAsState(
            if (isWeekMode) {
                weekCalendarSize.height
            } else {
                weekCalendarSize.height * weeksInVisibleMonth
            },
            tween(durationMillis = 250),
        )
        val density = LocalDensity.current
        Box {
            HorizontalCalendar(
                modifier = Modifier
                    .height(monthCalendarHeight)
                    .alpha(monthCalendarAlpha)
                    .zIndex(if (isWeekMode) 0f else 1f),
                state = monthState,
                dayContent = { day ->
                    Day(
                        day.date,
                        isSelected = selections.contains(day.date),
                        onClick = onClick,
                        day.date == currentDate,
                    )
                },
                monthHeader = {
                    MonthHeader(daysOfWeek = daysOfWeek)
                },
            )
            WeekCalendar(
                modifier = Modifier
                    .wrapContentHeight()
                    .onSizeChanged {
                        val size = density.run { DpSize(it.width.toDp(), it.height.toDp()) }
                        if (weekCalendarSize != size) {
                            weekCalendarSize = size
                        }
                    }
                    .alpha(weekCalendarAlpha)
                    .zIndex(if (isWeekMode) 1f else 0f),
                state = weekState,
                dayContent = { day ->
                    Day(
                        day.date,
                        isSelected = selections.contains(day.date),
                        onClick = onClick,
                        day.date == currentDate
                    )
                },
            )
        }
    }
}

package ru.rescqd.jetschedule.ui.screen.schedule.group.model

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class PairMoreInfoModelGroup(
    val id: Long,
    val nextDays: Flow<List<LocalDate>>,
    val prevDays: Flow<List<LocalDate>>,
    val subject: String,
    val teacher: String,
    val pairOrder: Int,
    val audience: String,
    val date: LocalDate
)


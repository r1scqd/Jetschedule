package ru.rescqd.jetschedule.ui.screen.schedule.teacher.model

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class PairMoreInfoModelTeacher(
    val id: Long,
    val nextDays: Flow<List<LocalDate>>,
    val prevDays: Flow<List<LocalDate>>,
    val subject: String,
    val teacher: String,
    val pairOrder: Int,
    val audience: String
)
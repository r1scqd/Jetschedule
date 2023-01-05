package ru.rescqd.jetschedule.ui.screen.schedule.teacher.model

import kotlinx.coroutines.flow.Flow
import ru.rescqd.jetschedule.ui.screen.schedule.shared.model.PairCardModel
import java.time.LocalDate

sealed class ScheduleTeacherViewState{
    object Loading: ScheduleTeacherViewState()
    data class Display(
        val date: LocalDate,
        val teacherName: String,
        val pairCardModelsFlow: Flow<List<PairCardModel>>,
        val pairMoreInfoModelFlow: PairMoreInfoModelTeacher? = null,
        val synchronizedDates: Flow<List<LocalDate>>
    ): ScheduleTeacherViewState()
}

package ru.rescqd.jetschedule.ui.screen.schedule.group.model

import kotlinx.coroutines.flow.Flow
import ru.rescqd.jetschedule.ui.screen.schedule.shared.model.PairCardModel
import java.time.LocalDate

sealed class ScheduleGroupViewState{
    object Loading: ScheduleGroupViewState()
    data class Display(
        val date: LocalDate,
        val groupName: String,
        val pairCardModelsFlow: Flow<List<PairCardModel>>,
        val pairMoreInfoModelFlow: PairMoreInfoModelGroup? = null,
        val synchronizedDates: Flow<List<LocalDate>>,
        val prevDate: LocalDate? = null
    ): ScheduleGroupViewState()
}

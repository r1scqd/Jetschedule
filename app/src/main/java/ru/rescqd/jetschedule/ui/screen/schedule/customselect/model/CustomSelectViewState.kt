package ru.rescqd.jetschedule.ui.screen.schedule.customselect.model

import kotlinx.coroutines.flow.Flow
import ru.rescqd.jetschedule.data.container.AudienceContainer
import java.time.LocalDate

sealed class CustomSelectViewState{
    data class Error(val message: String?) : CustomSelectViewState()
    object Loading: CustomSelectViewState()
    data class DisplayAudience(
        val audience: Flow<List<AudienceContainer>>,
        val filter: CustomSelectFilter,
        val filterInfo: FilterInfo,
        val synchronizedDate: Flow<List<LocalDate>>
        ): CustomSelectViewState()
}

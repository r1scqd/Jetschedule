package ru.rescqd.jetschedule.ui.screen.schedule.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.rescqd.jetschedule.data.local.repository.JetscheduleRepository
import ru.rescqd.jetschedule.ui.screen.base.EventHandler
import ru.rescqd.jetschedule.ui.screen.schedule.group.model.PairMoreInfoModelGroup
import ru.rescqd.jetschedule.ui.screen.schedule.group.model.ScheduleGroupEvent
import ru.rescqd.jetschedule.ui.screen.schedule.group.model.ScheduleGroupViewState
import ru.rescqd.jetschedule.ui.screen.schedule.shared.model.PairCardModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleGroupViewModel @Inject constructor(
    private val jetscheduleRepository: JetscheduleRepository,
) : ViewModel(), EventHandler<ScheduleGroupEvent> {
    private val _state =
        MutableStateFlow<ScheduleGroupViewState>(ScheduleGroupViewState.Loading)
    val state = _state.asStateFlow()
    override fun obtainEvent(event: ScheduleGroupEvent) {
        when (val currentState = state.value) {
            is ScheduleGroupViewState.Display -> reduce(event, currentState)
            is ScheduleGroupViewState.Loading -> reduce(event)
        }
    }

    private fun reduce(
        event: ScheduleGroupEvent,
    ) {
        when (event) {
            is ScheduleGroupEvent.DateSelected -> TODO()
            is ScheduleGroupEvent.EnterScreen -> initialFetch(event.payload.groupName)
            is ScheduleGroupEvent.GroupTitleClicked -> TODO()
            is ScheduleGroupEvent.PairClicked -> TODO()
            ScheduleGroupEvent.BackPressed -> TODO()
        }
    }

    private fun reduce(
        event: ScheduleGroupEvent,
        currentState: ScheduleGroupViewState.Display,
    ) {
        when (event) {
            is ScheduleGroupEvent.DateSelected -> changeDate(event.date, currentState)
            is ScheduleGroupEvent.EnterScreen -> if (currentState.groupName == event.payload.groupName) fetch(
                currentState) else initialFetch(event.payload.groupName)
            is ScheduleGroupEvent.GroupTitleClicked -> Unit
            is ScheduleGroupEvent.PairClicked -> pairClickProcess(event.scheduleUid, currentState)
            is ScheduleGroupEvent.BackPressed -> changeDate(currentState.prevDate!!,
                currentState,
                savePrevDate = false)
        }
    }

    private fun pairClickProcess(pairId: Long, state: ScheduleGroupViewState.Display) {
        viewModelScope.launch {
            val info = jetscheduleRepository.getPairMoreInfo(pairId)
            _state.emit(state.copy(pairMoreInfoModel = PairMoreInfoModelGroup(id = info.scheduleUid,
                subject = info.subjectDefault,
                pairOrder = info.pairOrder,
                teacher = "${info.teacherName} ${info.teacherPatronymic}",
                audience = info.audience,
                nextDays = jetscheduleRepository.getNextPairDatesWithSubject(pairId,
                    state.date,
                    state.groupName),
                prevDays = jetscheduleRepository.getPrevPairDatesWithSubject(pairId,
                    state.date,
                    state.groupName),
                date = state.date
            )))
        }
    }

    private fun changeDate(
        date: LocalDate,
        currentState: ScheduleGroupViewState.Display,
        savePrevDate: Boolean = true,
    ) {
        fetch(currentState.copy(date = date,
            prevDate = if (savePrevDate) currentState.date else null))
    }

    private fun getInitialDate(): LocalDate = LocalDate.now()


    private fun initialFetch(groupName: String) {
        viewModelScope.launch {
            val date = getInitialDate()
            _state.emit(ScheduleGroupViewState.Display(
                date = date,
                groupName = groupName,
                pairCardModelsFlow = jetscheduleRepository.getDefaultScheduleOnDate(groupName = groupName,
                    date = date).map { pairInfoContainers ->
                    pairInfoContainers.map {
                        with(it) {
                            PairCardModel(scheduleUid,
                                subjectCustom,
                                pairOrder,
                                teacherCustom,
                                audience)
                        }
                    }
                },
                synchronizedDates = jetscheduleRepository.getSynchronizedSchedule()
            ))
        }
    }

    private fun fetch(state: ScheduleGroupViewState.Display) {
        viewModelScope.launch {
            _state.emit(state.copy(pairCardModelsFlow = jetscheduleRepository.getDefaultScheduleOnDate(
                groupName = state.groupName,
                date = state.date).map { pairInfoContainers ->
                pairInfoContainers.map {
                    with(it) {
                        PairCardModel(scheduleUid,
                            subjectCustom,
                            pairOrder,
                            teacherCustom,
                            audience)
                    }
                }
            }))
        }
    }
}
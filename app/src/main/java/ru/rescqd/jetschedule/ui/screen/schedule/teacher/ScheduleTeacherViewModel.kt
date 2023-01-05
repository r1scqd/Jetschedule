package ru.rescqd.jetschedule.ui.screen.schedule.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.rescqd.jetschedule.data.local.repository.JetscheduleRepository
import ru.rescqd.jetschedule.ui.screen.base.EventHandler
import ru.rescqd.jetschedule.ui.screen.schedule.shared.model.PairCardModel
import ru.rescqd.jetschedule.ui.screen.schedule.teacher.model.PairMoreInfoModelTeacher
import ru.rescqd.jetschedule.ui.screen.schedule.teacher.model.ScheduleTeacherEvent
import ru.rescqd.jetschedule.ui.screen.schedule.teacher.model.ScheduleTeacherViewState
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class ScheduleTeacherViewModel @Inject constructor(
    private val jetscheduleRepository: JetscheduleRepository,
) : ViewModel(), EventHandler<ScheduleTeacherEvent> {
    private val _state =
        MutableStateFlow<ScheduleTeacherViewState>(ScheduleTeacherViewState.Loading)
    val state = _state.asStateFlow()
    override fun obtainEvent(event: ScheduleTeacherEvent) {
        when (val currentState = state.value) {
            is ScheduleTeacherViewState.Display -> reduce(event, currentState)
            is ScheduleTeacherViewState.Loading -> reduce(event)
        }
    }

    private val synchronizedDates = jetscheduleRepository.getSynchronizedSchedule()
    private fun reduce(
        event: ScheduleTeacherEvent,
    ) {
        when (event) {
            is ScheduleTeacherEvent.DateSelected -> TODO()
            is ScheduleTeacherEvent.EnterScreen -> initialFetch(event.payload.teacherName)
            is ScheduleTeacherEvent.TeacherTitleClicked -> TODO()
            is ScheduleTeacherEvent.PairClicked -> TODO()
        }
    }

    private fun reduce(
        event: ScheduleTeacherEvent,
        currentState: ScheduleTeacherViewState.Display,
    ) {
        when (event) {
            is ScheduleTeacherEvent.DateSelected -> changeDate(event.date, currentState)
            is ScheduleTeacherEvent.EnterScreen -> if (currentState.teacherName == event.payload.teacherName) fetch(
                currentState) else initialFetch(event.payload.teacherName)
            is ScheduleTeacherEvent.TeacherTitleClicked -> Unit
            is ScheduleTeacherEvent.PairClicked -> pairClickProcess(event.scheduleUid, currentState)
        }
    }

    private fun pairClickProcess(pairId: Long, state: ScheduleTeacherViewState.Display) {
        viewModelScope.launch {
            val info = jetscheduleRepository.getPairMoreInfo(pairId)
            _state.emit(state.copy(pairMoreInfoModelFlow = PairMoreInfoModelTeacher(id = info.scheduleUid,
                subject = info.subjectDefault,
                pairOrder = info.pairOrder,
                teacher = "${info.teacherName} ${info.teacherPatronymic}",
                audience = info.audience,
                nextDays = emptyFlow(),
                prevDays = emptyFlow())))
        }
    }

    private fun changeDate(date: LocalDate, currentState: ScheduleTeacherViewState.Display) {
        fetch(currentState.copy(date = date))
    }

    private fun getInitialDate(): LocalDate = LocalDate.of(2023, Month.JANUARY, 10)


    private fun initialFetch(teacherName: String) {
        viewModelScope.launch {
            val date = getInitialDate()
            _state.emit(ScheduleTeacherViewState.Display(
                date = date,
                teacherName = teacherName,
                pairCardModelsFlow = jetscheduleRepository.getDefaultScheduleOnDate(teacher = teacherName,
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
                }, synchronizedDates = synchronizedDates
            ))
        }
    }

    private fun fetch(state: ScheduleTeacherViewState.Display) {
        viewModelScope.launch {
            _state.emit(state.copy(pairCardModelsFlow = jetscheduleRepository.getDefaultScheduleOnDate(
                teacher = state.teacherName,
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
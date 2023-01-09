package ru.rescqd.jetschedule.ui.screen.schedule.manager

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.data.Contains
import ru.rescqd.jetschedule.data.local.repository.JetscheduleRepository
import ru.rescqd.jetschedule.ui.components.SnackbarManager
import ru.rescqd.jetschedule.ui.screen.base.EventHandler
import ru.rescqd.jetschedule.ui.screen.schedule.manager.model.ScheduleManagerEvent
import ru.rescqd.jetschedule.ui.screen.schedule.manager.model.ScheduleManagerMode
import ru.rescqd.jetschedule.ui.screen.schedule.manager.model.ScheduleManagerViewState
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleManagerViewModel @Inject constructor(
    private val jetscheduleRepository: JetscheduleRepository,
    private val snackBarManager: SnackbarManager,
    @ApplicationContext ctx: Context
) : ViewModel(), EventHandler<ScheduleManagerEvent> {

    private val defaultErrorMessage = ctx.getString(R.string.schedule_manager_default_error_message)

    private val _state =
        MutableStateFlow<ScheduleManagerViewState>(ScheduleManagerViewState.Loading)
    val state = _state.asStateFlow()

    override fun obtainEvent(event: ScheduleManagerEvent) {
        when (val currentState = state.value) {
            is ScheduleManagerViewState.Display -> reduce(event, currentState)
            is ScheduleManagerViewState.Error -> reduce(event, currentState)
            is ScheduleManagerViewState.Loading -> fetch()
        }
    }

    private fun reduce(
        event: ScheduleManagerEvent, currentState: ScheduleManagerViewState.Display
    ) {
        when (event) {
            is ScheduleManagerEvent.DateClicked -> clickDate(event.date, currentState.mode)
            is ScheduleManagerEvent.EnterScreen -> Unit
            is ScheduleManagerEvent.ModeChanged -> viewModelScope.launch {
                _state.emit(
                    currentState.copy(
                        mode = event.mode
                    )
                )
            }
            is ScheduleManagerEvent.ReloadScreen -> fetch()
        }
    }


    private fun reduce(event: ScheduleManagerEvent, currentState: ScheduleManagerViewState.Error) {
        when (event) {
            is ScheduleManagerEvent.DateClicked -> TODO()
            is ScheduleManagerEvent.EnterScreen -> Unit
            is ScheduleManagerEvent.ModeChanged -> TODO()
            is ScheduleManagerEvent.ReloadScreen -> fetch()
        }
    }

    private fun clickDate(date: LocalDate, mode: ScheduleManagerMode) {
        viewModelScope.launch(Dispatchers.IO) {
            when (mode) {
                ScheduleManagerMode.REMOVE -> jetscheduleRepository.dropScheduleOnDate(date)
                ScheduleManagerMode.ADD -> syncOnDate(date)
            }
        }
    }

    private fun syncOnDate(date: LocalDate) {
        viewModelScope.launch {
            Log.d("JET_WORK_DEBUG", "syncOnDate with $date")
            val query = jetscheduleRepository.syncOnDate(date)
            query.workInfosLiveData.observeForever { workInfos ->
                workInfos.forEach {
                    if (it.state == WorkInfo.State.FAILED) {
                        val message = it.outputData.getString(Contains.WORK_FAILED_ERROR_KEY)
                            ?: defaultErrorMessage
                        snackBarManager.showMessage(message)
                    }
                }
            }
        }
    }


    private fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.emit(
                    ScheduleManagerViewState.Display(
                        jetscheduleRepository.getSynchronizedSchedule(), ScheduleManagerMode.ADD
                    )
                )
            } catch (e: Exception) {
                _state.emit(ScheduleManagerViewState.Error(e.message.toString()))
            }
        }
    }
}
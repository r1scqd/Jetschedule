package ru.rescqd.jetschedule.ui.screen.schedule.customselect

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.data.Contains
import ru.rescqd.jetschedule.data.local.repository.JetscheduleRepository
import ru.rescqd.jetschedule.data.local.repository.UserPreferencesRepository
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.model.CustomSelectFilter
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.model.CustomSelectViewState
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.model.FilterInfo
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalSerializationApi
@HiltViewModel
class CustomSelectViewModel @Inject constructor(
    private val jetscheduleRepository: JetscheduleRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CustomSelectViewState>(CustomSelectViewState.Loading)
    val state: StateFlow<CustomSelectViewState> = _state

    private val synchronizedDates = jetscheduleRepository.getSynchronizedSchedule()

    private var currentCorpus: Int = 1

    init {
        viewModelScope.launch {
            currentCorpus = userPreferencesRepository.getScheduleCorpus(currentCorpus).first()
        }
    }

    fun enterScreen() {
        viewModelScope.launch {
            when (val currentState = state.value) {
                is CustomSelectViewState.DisplayAudience -> fetch(
                    currentState.filter,
                    currentState.filterInfo
                )
                is CustomSelectViewState.Error -> Unit
                CustomSelectViewState.Loading -> fetch(getInitialFilter(), getInitialFilterInfo())
            }
        }
    }

    fun dateClicked(localDate: LocalDate) {
        when (val currentState = state.value) {
            is CustomSelectViewState.DisplayAudience -> fetch(
                currentState.filter,
                currentState.filterInfo.copy(date = localDate)
            )
            is CustomSelectViewState.Error -> TODO()
            CustomSelectViewState.Loading -> TODO()
        }
    }

    fun pairOrderClicked(pairOrder: Int) {
        when (val currentState = state.value) {
            is CustomSelectViewState.DisplayAudience -> fetch(
                currentState.filter,
                currentState.filterInfo.copy(pairOrder = pairOrder)
            )
            is CustomSelectViewState.Error -> TODO()
            CustomSelectViewState.Loading -> TODO()
        }
    }

    fun corpusSelected(corpus: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setScheduleCorpus(corpus)
        }
        when (val currentState = state.value) {
            is CustomSelectViewState.DisplayAudience -> fetch(
                currentState.filter,
                currentState.filterInfo.copy(corpus = corpus)
            )
            is CustomSelectViewState.Error -> TODO()
            CustomSelectViewState.Loading -> TODO()
        }
    }

    private fun getInitialFilter(): CustomSelectFilter = CustomSelectFilter.ALL_AUDIENCE


    private fun getInitialFilterInfo(): FilterInfo =
        FilterInfo(
            date = LocalDate.now(),
            pairOrder = 0,
            corpus = currentCorpus
        )

    fun filterCLicked(customSelectFilter: CustomSelectFilter) {
        when (val currentState = state.value) {
            is CustomSelectViewState.DisplayAudience -> fetch(
                customSelectFilter,
                currentState.filterInfo
            )
            is CustomSelectViewState.Error -> TODO()
            CustomSelectViewState.Loading -> TODO()
        }
    }

    private fun fetch(filter: CustomSelectFilter, filterInfo: FilterInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = when (filter) {
                    CustomSelectFilter.BUSY_AUDIENCE_BY_DATE -> jetscheduleRepository.getBusyAudience(
                        filterInfo.corpus!!,
                        date = filterInfo.date!!
                    )
                    CustomSelectFilter.AVAILABLE_AUDIENCE_BY_DATE_AND_PAIR_ORDER -> jetscheduleRepository.getAvailableAudience(
                        filterInfo.corpus!!, date = filterInfo.date!!,
                        filterInfo.pairOrder!!
                    )
                    CustomSelectFilter.AVAILABLE_AUDIENCE_BY_DATE -> jetscheduleRepository.getAvailableAudience(
                        filterInfo.corpus!!, date = filterInfo.date!!
                    )
                    CustomSelectFilter.ALL_AUDIENCE -> jetscheduleRepository.getAllAudience(filterInfo.corpus!!)
                }
                Log.d(Contains.DEBUG, data.first().toString())
                _state.emit(
                    CustomSelectViewState.DisplayAudience(
                        data,
                        filter,
                        filterInfo,
                        synchronizedDates
                    )
                )
            } catch (e: Exception){
                Log.d(Contains.DEBUG, e.message.toString())
                _state.emit(CustomSelectViewState.Error(e.message))
            }
        }
    }

}

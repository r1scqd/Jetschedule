package ru.rescqd.jetschedule.ui.screen.settings.behavior

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.data.local.repository.JetscheduleRepository
import ru.rescqd.jetschedule.data.local.repository.UserPreferencesRepository
import ru.rescqd.jetschedule.data.utils.isNumber
import ru.rescqd.jetschedule.ui.components.SnackbarManager
import ru.rescqd.jetschedule.ui.home.HomeSections
import ru.rescqd.jetschedule.ui.screen.settings.behavior.model.SettingsBehaviorViewState
import ru.rescqd.jetschedule.ui.screen.settings.behavior.model.SettingsContainer
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalSerializationApi::class)
class SettingsBehaviorViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val snackBarManager: SnackbarManager,
    private val jetscheduleRepository: JetscheduleRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<SettingsBehaviorViewState>(SettingsBehaviorViewState.Loading)

    val state: StateFlow<SettingsBehaviorViewState> = _state

    fun enterScreen() {
        when (state.value) {
            is SettingsBehaviorViewState.Display -> Unit
            SettingsBehaviorViewState.Loading -> fetch()
        }
    }

    private fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(
                SettingsBehaviorViewState.Display(
                    userPreferencesRepository.getScheduleBackendProvider().first(),
                    userPreferencesRepository.getIsPeriodicWorkEnabled().first(),
                    userPreferencesRepository.getPeriodicWorkRepeatInterval(Duration.ofHours(1))
                        .first(),
                    userPreferencesRepository.getPeriodicWorkFlexInterval(Duration.ofMinutes(15))
                        .first(),
                    userPreferencesRepository.getPeriodicWorkThreshold(1L).first(),
                    userPreferencesRepository.getPeriodicWorkDateOffset(0L).first(),
                    userPreferencesRepository.getStartDestination(HomeSections.SETTINGS).first()
                )
            )
        }
    }

    fun confirmSettings(
        newState: SettingsContainer
    ) {
        if (!isValidSettingsContainer(newState))
            return
        when (val currentState = state.value) {
            is SettingsBehaviorViewState.Display -> confirmSettings(currentState, newState)
            else -> throw NotImplementedError()
        }

    }

    private fun isValidSettingsContainer(
        newState: SettingsContainer
    ): Boolean {
        if (!newState.isPeriodicWorkEnabled)
            return true
        if (!newState.dateOffset.isNumber()) {
            snackBarManager.showMessage(R.string.snack_invalid_date_offset)
            return false
        }
        if (!newState.dateThreshold.isNumber() || newState.dateThreshold.toLong() < 1) {
            snackBarManager.showMessage(R.string.snack_invalid_date_threshold)
            return false
        }
        return true
    }

    private fun confirmSettings(
        oldState: SettingsBehaviorViewState.Display,
        newState: SettingsContainer
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (oldState.startDestination != newState.startDestination) {
                userPreferencesRepository.setStartDestination(newState.startDestination)
            }
            if (oldState.backendProvider != newState.backendProvider) {
                userPreferencesRepository.setScheduleBackendProvider(newState.backendProvider)
            }
            userPreferencesRepository.setIsPeriodicWorkEnabled(newState.isPeriodicWorkEnabled)

            if (newState.isPeriodicWorkEnabled) {
                userPreferencesRepository.setPeriodicWorkRepeatInterval(newState.repeatInterval)
                userPreferencesRepository.setPeriodicWorkFlexInterval(newState.flexInterval)
                userPreferencesRepository.setPeriodicWorkDateOffset(newState.dateOffset.toLong())
                userPreferencesRepository.setPeriodicWorkThreshold(newState.dateThreshold.toLong())
            }
            configurePeriodicSyncSchedule()
            snackBarManager.showMessage(R.string.snack_save_settings)
        }
    }

    private fun configurePeriodicSyncSchedule() {
        viewModelScope.launch { jetscheduleRepository.reloadScheduleAutoDownload() }
    }
}
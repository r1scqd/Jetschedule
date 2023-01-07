package ru.rescqd.jetschedule.ui.screen.settings.rename.subject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.data.container.SubjectContainer
import ru.rescqd.jetschedule.data.local.repository.SchedulePreferencesRepository
import ru.rescqd.jetschedule.ui.components.SnackbarManager
import ru.rescqd.jetschedule.ui.screen.base.EventHandler
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model.RenameSubjectError
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model.RenameSubjectEvent
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model.RenameSubjectViewState
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model.SubjectModel
import javax.inject.Inject

@HiltViewModel
class RenameSubjectViewModel @Inject constructor(
    private val schedulePreferencesRepository: SchedulePreferencesRepository,
    private val snackBarManager: SnackbarManager
) : ViewModel(), EventHandler<RenameSubjectEvent> {

    private val _state = MutableStateFlow<RenameSubjectViewState>(RenameSubjectViewState.Loading)
    val state = _state.asStateFlow()

    override fun obtainEvent(event: RenameSubjectEvent) {
        when (val currentState = state.value) {
            is RenameSubjectViewState.Loading -> reduce(event, currentState)
            is RenameSubjectViewState.DisplaySubjectSelect -> reduce(event, currentState)
        }
    }

    private fun reduce(
        event: RenameSubjectEvent,
        currentState: RenameSubjectViewState.DisplaySubjectSelect
    ) {
        when (event) {
            is RenameSubjectEvent.EnterScreen -> fetch()
            is RenameSubjectEvent.ConfirmClicked -> saveChanges(currentState)
            is RenameSubjectEvent.SubjectDisplayNameChange -> displayNameChange(
                event.newDisplayName,
                currentState
            )
            is RenameSubjectEvent.SubjectSelected -> viewModelScope.launch {
                _state.emit(
                    currentState.copy(currentSubject = event.subjectModel, sendingError = null)
                )
            }
        }
    }

    private fun displayNameChange(
        newDisplayName: String,
        currentState: RenameSubjectViewState.DisplaySubjectSelect
    ) {
        viewModelScope.launch {
            currentState.currentSubject?.let {
                _state.emit(currentState.copy(currentSubject = it.copy(displayName = newDisplayName)))
                return@launch
            }
            _state.emit(currentState.copy(sendingError = RenameSubjectError.SubjectNotSelected))
        }
    }

    private fun saveChanges(currentState: RenameSubjectViewState.DisplaySubjectSelect) {
        viewModelScope.launch {
            currentState.currentSubject?.let {
                schedulePreferencesRepository.updateSubject(
                    it.uid,
                    it.displayName.ifBlank { it.name }
                )
                _state.emit(currentState.copy(sendingError = RenameSubjectError.Success))
                return@launch
            }
            _state.emit(currentState.copy(sendingError = RenameSubjectError.SubjectNotSelected))
        }
    }

    private fun SubjectContainer.asSubjectModel(): SubjectModel =
        SubjectModel(uid, defaultName, displayName)

    private fun reduce(event: RenameSubjectEvent, currentState: RenameSubjectViewState.Loading) {
        when (event) {
            is RenameSubjectEvent.EnterScreen -> fetch()
            is RenameSubjectEvent.ConfirmClicked -> snackBarManager.showMessage(R.string.snack_not_implemented)
            is RenameSubjectEvent.SubjectDisplayNameChange -> snackBarManager.showMessage(R.string.snack_not_implemented)
            is RenameSubjectEvent.SubjectSelected -> snackBarManager.showMessage(R.string.snack_not_implemented)
        }
    }

    private fun fetch() {
        viewModelScope.launch {
            _state.emit(
                RenameSubjectViewState.DisplaySubjectSelect(
                    schedulePreferencesRepository.getAllSubjects()
                        .map { subjectContainers -> subjectContainers.map { it.asSubjectModel() } }.first()
                )
            )
        }
    }

}
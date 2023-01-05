package ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.data.local.repository.JetscheduleRepository
import ru.rescqd.jetschedule.ui.components.SnackbarManager
import ru.rescqd.jetschedule.ui.screen.base.EventHandler
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model.*
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemPayload
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemType
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
class AddDrawerViewModel @Inject constructor(
    private val jetscheduleRepository: JetscheduleRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel(), EventHandler<AddDrawerEvent> {

    fun resetState() {
        viewModelScope.launch {
            _state.emit(AddDrawerViewState.Loading)
        }
    }

    private val _state = MutableStateFlow<AddDrawerViewState>(AddDrawerViewState.Loading)
    val state = _state.asStateFlow()
    override fun obtainEvent(event: AddDrawerEvent) {
        when (val currentState = state.value) {
            is AddDrawerViewState.DisplaySelectType -> reduce(event, currentState)
            is AddDrawerViewState.Loading -> reduce(event)
            is AddDrawerViewState.DisplayGroupSelect -> reduce(event, currentState)
            is AddDrawerViewState.DisplayTeacherSelect -> reduce(event, currentState)
            is AddDrawerViewState.DialogConfirmExit -> throw IllegalStateException()
        }
    }


    private fun reduce(event: AddDrawerEvent) {
        when (event) {
            is AddDrawerEvent.EnterScreen -> initialEnterScreen()
            else -> throw NotImplementedError()
        }
    }


    private fun reduce(event: AddDrawerEvent, currentState: AddDrawerViewState.DisplayGroupSelect) {
        when (event) {
            is AddDrawerEvent.EnterScreen -> initialEnterScreen()
            is AddDrawerEvent.ItemTypeSelected -> TODO()
            is AddDrawerEvent.GroupSelected -> groupSelected(event.group, currentState)
            is AddDrawerEvent.TeacherSelected -> TODO()
            is AddDrawerEvent.ConfirmClicked -> confirmClicked(currentState)
        }
    }

    private fun reduce(event: AddDrawerEvent, currentState: AddDrawerViewState.DisplaySelectType) {
        when (event) {
            is AddDrawerEvent.EnterScreen -> initialEnterScreen()
            is AddDrawerEvent.ItemTypeSelected -> itemTypeSelected(event.itemType)
            is AddDrawerEvent.GroupSelected -> TODO()
            is AddDrawerEvent.TeacherSelected -> TODO()
            AddDrawerEvent.ConfirmClicked -> TODO()
        }
    }

    private fun reduce(
        event: AddDrawerEvent,
        currentState: AddDrawerViewState.DisplayTeacherSelect,
    ) {
        when (event) {
            is AddDrawerEvent.EnterScreen -> Unit
            is AddDrawerEvent.ItemTypeSelected -> TODO()
            is AddDrawerEvent.GroupSelected -> TODO()
            is AddDrawerEvent.TeacherSelected -> teacherSelected(event.teacher, currentState)
            AddDrawerEvent.ConfirmClicked -> confirmClicked(currentState)
        }
    }

    private fun confirmClicked(currentState: AddDrawerViewState.DisplayTeacherSelect) {
        viewModelScope.launch {
            if (currentState.selectedTeacher == null) {
                snackbarManager.showMessage(R.string.add_drawer_select_teacher_not_selected)
            } else
                _state.emit(
                    AddDrawerViewState.DialogConfirmExit(
                        label = currentState.selectedTeacher.name,
                        type = LeftNavigationDrawerItemType.TEACHER,
                        payload = LeftNavigationDrawerItemPayload.TeacherPayload(currentState.selectedTeacher.name)
                    )
                )
        }
    }

    private fun confirmClicked(currentState: AddDrawerViewState.DisplayGroupSelect) {
        viewModelScope.launch {
            if (currentState.selectedGroup == null) {
                snackbarManager.showMessage(R.string.add_drawer_select_group_not_selected)
            } else
                _state.emit(
                    AddDrawerViewState.DialogConfirmExit(
                        label = currentState.selectedGroup.name,
                        type = LeftNavigationDrawerItemType.GROUP,
                        payload = LeftNavigationDrawerItemPayload.GroupPayload(currentState.selectedGroup.name)
                    )
                )

        }
    }


    private fun getInitialLabel(): String = ""

    private fun itemTypeSelected(
        itemType: AddDrawerItemType,
    ) {
        viewModelScope.launch {
            when (itemType) {
                AddDrawerItemType.TEACHER -> _state.emit(
                    AddDrawerViewState.DisplayTeacherSelect(
                        label = getInitialLabel(),
                        teachers = jetscheduleRepository.getAllTeacher().first()
                            .map { TeacherModel(it) },
                    )
                )
                AddDrawerItemType.GROUP -> _state.emit(AddDrawerViewState.DisplayGroupSelect(
                    label = getInitialLabel(),
                    groups = jetscheduleRepository.getAllGroups().first().map { GroupModel(it) }
                ))
            }
        }
    }

    private fun initialEnterScreen() {
        viewModelScope.launch {
            _state.emit(AddDrawerViewState.DisplaySelectType)
        }
    }

    private fun groupSelected(
        group: GroupModel,
        currentState: AddDrawerViewState.DisplayGroupSelect,
    ) {
        viewModelScope.launch {
            _state.emit(
                currentState.copy(selectedGroup = group)
            )
        }
    }

    private fun teacherSelected(
        teacherModel: TeacherModel,
        currentState: AddDrawerViewState.DisplayTeacherSelect,
    ) {
        viewModelScope.launch {
            _state.emit(
                currentState.copy(selectedTeacher = teacherModel)
            )
        }
    }
}
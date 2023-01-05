package ru.rescqd.jetschedule.ui.screen.schedule.schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.data.local.repository.UserPreferencesRepository
import ru.rescqd.jetschedule.ui.components.Message
import ru.rescqd.jetschedule.ui.components.SnackbarManager
import ru.rescqd.jetschedule.ui.screen.base.EventHandler
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.*
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
@HiltViewModel
class MainScheduleViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val snackbarManager: SnackbarManager,
    @ApplicationContext ctx: Context
) : ViewModel(), EventHandler<ScheduleEvent> {

    private val _state = MutableStateFlow<ScheduleViewState>(ScheduleViewState.Loading)
    val state = _state.asStateFlow()

    override fun obtainEvent(event: ScheduleEvent) {
        when (val currentState = _state.value) {
            is ScheduleViewState.Display -> reduce(event, currentState)
            is ScheduleViewState.Loading -> initialFetch()
        }
    }

    private fun reduce(event: ScheduleEvent, currentState: ScheduleViewState.Display) {
        when (event) {
            is ScheduleEvent.EnterScreen -> Unit
            is ScheduleEvent.LeftNavigationDrawerItemChanged -> changeItem(event.item, currentState)
            is ScheduleEvent.LeftNavigationDrawerItemAdd -> addItem(event.newItem, currentState)
            is ScheduleEvent.LeftNavigationDrawerItemDelete -> deleteItem(event.deleteItem,
                currentState)
        }
    }

    private fun changeItem(
        item: LeftNavigationDrawerItem,
        currentState: ScheduleViewState.Display,
    ) {
        if (currentState.currentItem == item) return
        viewModelScope.launch {
            if (!currentState.items.contains(item)) {
                snackbarManager.showMessage(Message(messageId = R.string.error_change_drawer_item))
            } else {
                userPreferencesRepository.setCurrentLeftNavDrawerItem(item)
                _state.emit(currentState.copy(currentItem = item))
            }
        }
    }

    private fun addItem(
        newItem: LeftNavigationDrawerItem,
        currentState: ScheduleViewState.Display,
    ) {
        viewModelScope.launch {
            val newItems = currentState.items.toMutableList()
            newItems.add(newItem)
            userPreferencesRepository.setLeftNavDrawerItems(newItems)
            _state.emit(currentState.copy(items = newItems))
        }
    }

    private fun deleteItem(
        item: LeftNavigationDrawerItem,
        currentState: ScheduleViewState.Display,
    ) {
        viewModelScope.launch {
            if (!currentState.items.contains(item)) {
                snackbarManager.showMessage(Message(messageId = R.string.error_delete_drawer_item))
            } else {
                val items = currentState.items.toMutableList()
                items.remove(item)
                userPreferencesRepository.setLeftNavDrawerItems(items)
                _state.emit(currentState.copy(items = items))
            }
        }
    }

    private val leftNavDrawerItemsFlow = userPreferencesRepository.getLeftNavDrawerItems()
    private val preloadedLeftNavBarItem = LeftNavigationDrawerItem(ctx.getString(R.string.schedule_left_navigation_drawer_custom_select_title),
        itemType = LeftNavigationDrawerItemType.CUSTOM_SELECT,
        itemPayload = LeftNavigationDrawerItemPayload.CustomSelectPayload,
        deletable = false)

    private suspend fun loadingLeftNavDrawerItems(): List<LeftNavigationDrawerItem> {
        val items = leftNavDrawerItemsFlow.first()
        if (items.isEmpty()) {
            userPreferencesRepository.setLeftNavDrawerItems(listOf(preloadedLeftNavBarItem))
        }
        return leftNavDrawerItemsFlow.first()
    }

    private suspend fun loadingCurrentLeftNavDrawerItem(): LeftNavigationDrawerItem {
        return userPreferencesRepository.getCurrentLeftNavDrawerItem(preloadedLeftNavBarItem)
            .first()
    }

    private fun initialFetch() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(ScheduleViewState.Display(items = loadingLeftNavDrawerItems(),
                currentItem = loadingCurrentLeftNavDrawerItem()))
        }
    }


}
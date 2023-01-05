package ru.rescqd.jetschedule.ui.screen.settings.behavior.model

import ru.rescqd.jetschedule.ui.theme.ScheduleBackendProvider
import ru.rescqd.jetschedule.ui.home.HomeSections
import java.time.Duration

sealed class SettingsBehaviorViewState{
    object Loading: SettingsBehaviorViewState()
    data class Display(
        val backendProvider: ScheduleBackendProvider,
        val isPeriodicWorkEnabled: Boolean,
        val repeatInterval: Duration,
        val flexInterval: Duration,
        val dateThreshold: Long,
        val dateOffset: Long,
        val startDestination: HomeSections
    ): SettingsBehaviorViewState()
}

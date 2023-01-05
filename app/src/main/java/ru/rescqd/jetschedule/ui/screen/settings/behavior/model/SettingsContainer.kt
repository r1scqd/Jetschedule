package ru.rescqd.jetschedule.ui.screen.settings.behavior.model

import ru.rescqd.jetschedule.ui.theme.ScheduleBackendProvider
import ru.rescqd.jetschedule.ui.home.HomeSections
import java.time.Duration

data class SettingsContainer(
    val backendProvider: ScheduleBackendProvider,
    val isPeriodicWorkEnabled: Boolean,
    val repeatInterval: Duration,
    val flexInterval: Duration,
    val dateThreshold: String,
    val dateOffset: String,
    val startDestination: HomeSections
)

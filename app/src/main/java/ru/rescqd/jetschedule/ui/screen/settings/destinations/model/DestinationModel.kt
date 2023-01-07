package ru.rescqd.jetschedule.ui.screen.settings.destinations.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.SettingsDestinations


enum class DestinationModel (
    @StringRes val resId: Int,
    val route: String,
    val icon: ImageVector? = null
){
    APPEARANCE(R.string.settings_screen_appearance, SettingsDestinations.APPLICATION_APPEARANCE.route),
    RENAME_SUBJECT(R.string.settings_screen_rename_subject, SettingsDestinations.RENAME_SUBJECT.route),
    BEHAVIOR(R.string.settings_screen_behavior, SettingsDestinations.BEHAVIOR.route),
    ABOUT(R.string.settings_screen_about, SettingsDestinations.ABOUT.route),
}
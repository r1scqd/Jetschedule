package ru.rescqd.jetschedule.ui.screen.settings.destinations.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.MainDestinations
import ru.rescqd.jetschedule.ui.SettingsDestinations


enum class DestinationModel(
    @StringRes val titleId: Int,
    val route: String,
    @StringRes val subtitleId: Int? = null,
    val icon: ImageVector? = null
) {
    APPEARANCE(
        R.string.settings_screen_appearance,
        SettingsDestinations.APPLICATION_APPEARANCE.route,
        R.string.settings_screen_appearance_subtitle,
        Icons.Outlined.DashboardCustomize
    ),
    RENAME_SUBJECT(
        R.string.settings_screen_rename_subject,
        SettingsDestinations.RENAME_SUBJECT.route,
        R.string.settings_screen_rename_subject_subtitle,
        Icons.Outlined.ChangeCircle
    ),
    BEHAVIOR(
        R.string.settings_screen_behavior,
        SettingsDestinations.BEHAVIOR.route,
        R.string.settings_screen_behavior_subtitle,
        Icons.Outlined.SettingsApplications
    ),
    GUIDE(
        R.string.settings_screen_manual,
        MainDestinations.MANUAL_ROUTE,
        R.string.settings_screen_manual_subtitle,
        Icons.Outlined.Bookmark
    ),
    ABOUT(
        R.string.settings_screen_about,
        SettingsDestinations.ABOUT.route,
        R.string.app_name,
        Icons.Outlined.Info
    ),
}
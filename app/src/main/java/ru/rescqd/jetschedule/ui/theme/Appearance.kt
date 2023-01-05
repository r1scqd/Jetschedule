package ru.rescqd.jetschedule.ui.theme

import androidx.annotation.StringRes
import ru.rescqd.jetschedule.R

enum class ScheduleBackendProvider(@StringRes val displayName: Int) {
    TEACHER(R.string.schedule_backend_prover_teacher), GROUP(R.string.schedule_backend_prover_group)
}

enum class JetscheduleFontFamily(@StringRes val displayName: Int) {
    SystemDefault(R.string.jetschedule_font_family_system_default), Montserrat(R.string.jetschedule_font_family_motserrat)
}

enum class JetscheduleColorScheme(@StringRes val displayName: Int) {
    DynamicColorScheme(R.string.jetschedule_color_scheme_dynamic), ColorSchemeVariant1(R.string.jetschedule_color_scheme_variant_1), ColorSchemeVariant2(
        R.string.jetschedule_color_scheme_variant_2
    ),
    AndroidTheme(R.string.jetschedule_color_android_theme)
}

enum class JetscheduleAudienceColorScheme(@StringRes val displayName: Int) {
    Dynamic(R.string.jetschedule_audience_color_scheme_dynamic), Variant1(R.string.jetschedule_audience_color_scheme_variant_1),
    Variant2(R.string.jetschedule_audience_color_scheme_variant_2),
    Android(R.string.jetschedule_audience_color_scheme_android)
}

enum class AppTheme(@StringRes val displayName: Int) {
    SYSTEM(R.string.app_theme_system),
    LIGHT(R.string.app_theme_light),
    NIGHT(R.string.app_theme_night)
}

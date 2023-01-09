package ru.rescqd.jetschedule.ui.screen.schedule.shared

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

val LocalDate.toPrettyString: String
    get() = "$dayOfMonth ${
        month.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        )
    }"
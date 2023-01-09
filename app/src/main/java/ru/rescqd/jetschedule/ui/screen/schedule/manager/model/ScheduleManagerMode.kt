package ru.rescqd.jetschedule.ui.screen.schedule.manager.model

import androidx.annotation.StringRes
import ru.rescqd.jetschedule.R

enum class ScheduleManagerMode( @StringRes val displayName: Int){
    REMOVE(R.string.schedule_manager_mode_remove), ADD(R.string.schedule_manager_mode_add)
}
package ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model

import androidx.annotation.StringRes
import ru.rescqd.jetschedule.R

enum class AddDrawerItemType(
    @StringRes val displayName: Int
){
    TEACHER(R.string.drawer_type_teacher)
    , GROUP(R.string.drawer_type_group)
}
package ru.rescqd.jetschedule.ui.screen.schedule.customselect.model

import androidx.annotation.StringRes
import ru.rescqd.jetschedule.R
import java.time.LocalDate


enum class CustomSelectFilter(
    @StringRes val resId: Int,
    val filterItems: List<CustomSelectFilterItems>
) {
    BUSY_AUDIENCE_BY_DATE(
        R.string.custom_select_filter_busy_audience_by_date,
        listOf(CustomSelectFilterItems.DATE, CustomSelectFilterItems.CORPUS)
    ),
    AVAILABLE_AUDIENCE_BY_DATE_AND_PAIR_ORDER(
        R.string.custom_select_filter_available_audience_by_date_and_pair_order,
        listOf(CustomSelectFilterItems.DATE, CustomSelectFilterItems.PAIR_ORDER, CustomSelectFilterItems.CORPUS)
    ),
    AVAILABLE_AUDIENCE_BY_DATE(
        R.string.custom_select_filter_available_audience_by_date,
        listOf(CustomSelectFilterItems.DATE, CustomSelectFilterItems.CORPUS)
    ),
    ALL_AUDIENCE(R.string.custom_select_filter_all_audience, listOf(CustomSelectFilterItems.CORPUS))
}

enum class CustomSelectFilterItems {
    DATE, CORPUS, PAIR_ORDER
}

data class FilterInfo(
    val date: LocalDate? = null,
    val pairOrder: Int? = null,
    val corpus: Int? = null
)
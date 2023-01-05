package ru.rescqd.jetschedule.ui.screen.schedule.customselect.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.Flow
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.data.container.AudienceContainer
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.NiaDropdownMenuButton
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.model.CustomSelectFilter
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.model.CustomSelectFilterItems
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.model.CustomSelectViewState
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.model.FilterInfo
import ru.rescqd.jetscheduleo.ui.components.NiaOutlinedButton
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewDisplayAudience(
    modifier: Modifier,
    state: CustomSelectViewState.DisplayAudience,
    onFilterClick: (CustomSelectFilter) -> Unit,
    onDateSelect: (LocalDate) -> Unit,
    onPairOrderSelect: (Int) -> Unit,
    onCorpusSelect: (Int) -> Unit,
) {
    JetscheduleScaffold(modifier = modifier.fillMaxSize(), bottomBar = {
        DisplayFilters(
            filterInfo = state.filterInfo,
            filter = state.filter,
            modifier = modifier,
            onFilterClick = onFilterClick,
            onDateSelect = onDateSelect,
            onPairOrderSelect = onPairOrderSelect,
            onCorpusSelect = onCorpusSelect,
            synchronizedDateFlow = state.synchronizedDate
        )
    }, content = { Box(modifier = modifier.padding(it)){DisplayAudience(audience = state.audience, modifier = modifier) }})
}

@Composable
private fun DisplayAudience(
    modifier: Modifier = Modifier, audience: Flow<List<AudienceContainer>>
) {
    val audienceState = audience.collectAsState(initial = emptyList())
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        LazyColumn(modifier.weight(1f)) {
            items(audienceState.value.filter { it.floor == 1 }) { item: AudienceContainer ->
                Text(
                    modifier = modifier,
                    text = item.audience,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        LazyColumn(modifier.weight(1f)) {
            items(audienceState.value.filter { it.floor == 2 }) { item: AudienceContainer ->
                Text(
                    modifier = modifier,
                    text = item.audience,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        LazyColumn(modifier.weight(1f)) {
            items(audienceState.value.filter { it.floor == 3 }) { item: AudienceContainer ->
                Text(
                    modifier = modifier,
                    text = item.audience,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun DisplayFilters(
    filterInfo: FilterInfo,
    filter: CustomSelectFilter,
    modifier: Modifier,
    onFilterClick: (CustomSelectFilter) -> Unit,
    onDateSelect: (LocalDate) -> Unit,
    onPairOrderSelect: (Int) -> Unit,
    onCorpusSelect: (Int) -> Unit,
    synchronizedDateFlow: Flow<List<LocalDate>>,
) {
    val synchronizedDate = synchronizedDateFlow.collectAsState(initial = emptyList())
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(res = R.string.button_action_confirm)
            negativeButton(res = R.string.button_action_cancel)
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium
    ) {
        datepicker(initialDate = filterInfo.date ?: LocalDate.now(),
            title = stringResource(id = R.string.custom_select_date_picker_title),
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.background,
                headerTextColor = MaterialTheme.colorScheme.primary,
                calendarHeaderTextColor = MaterialTheme.colorScheme.secondary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                dateActiveTextColor = MaterialTheme.colorScheme.primary,
                dateInactiveBackgroundColor = Color.Transparent,
                dateInactiveTextColor = MaterialTheme.colorScheme.secondary,
            ),
            onDateChange = onDateSelect,
            allowedDateValidator = { synchronizedDate.value.contains(it) })
    }
    Column(modifier) {
        NiaDropdownMenuButton(modifier = modifier.fillMaxWidth(),
            items = CustomSelectFilter.values().toList(),
            onItemClick = onFilterClick,
            text = { Text(stringResource(id = filter.resId)) },
            itemText = { Text(stringResource(id = it.resId)) })
        NiaDropdownMenuButton(
            modifier = modifier.fillMaxWidth(),
            items = (1..2).toList(),
            onItemClick = onCorpusSelect,
            text = {
                Text(
                    modifier = modifier,
                    text = "${stringResource(id = R.string.custom_select_dropdown_menu_corpus)}: ${filterInfo.corpus}"
                )
            },
            itemText = { Text(modifier = modifier, text = it.toString()) },
            enabled = filter.filterItems.contains(CustomSelectFilterItems.CORPUS)
        )
        NiaDropdownMenuButton(
            modifier = modifier.fillMaxWidth(),
            items = (1..7).toList(),
            onItemClick = onPairOrderSelect,
            text = {
                Text(
                    modifier = modifier,
                    text = "${stringResource(id = R.string.custom_select_dropdown_menu_pair_order)}: ${filterInfo.pairOrder}"
                )
            },
            itemText = { Text(modifier = modifier, text = it.toString()) },
            enabled = filter.filterItems.contains(CustomSelectFilterItems.PAIR_ORDER)
        )
        NiaOutlinedButton(modifier = modifier.fillMaxWidth(),
            onClick = { dialogState.show() },
            text = {
                Text(
                    modifier = modifier,
                    text = "${stringResource(id = R.string.custom_select_outline_button_date)}: ${filterInfo.date}"
                )
            },
            enabled = filter.filterItems.contains(CustomSelectFilterItems.DATE)
        )
    }
}
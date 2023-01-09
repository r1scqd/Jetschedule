package ru.rescqd.jetschedule.ui.screen.schedule.group

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.components.calendar.WeekCalendar
import ru.rescqd.jetschedule.ui.screen.schedule.group.model.PairMoreInfoModelGroup
import ru.rescqd.jetschedule.ui.screen.schedule.group.model.ScheduleGroupEvent
import ru.rescqd.jetschedule.ui.screen.schedule.group.model.ScheduleGroupViewState
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemPayload
import ru.rescqd.jetschedule.ui.screen.schedule.shared.toPrettyString
import ru.rescqd.jetschedule.ui.screen.schedule.shared.view.BottomSheetDateItem
import ru.rescqd.jetschedule.ui.screen.schedule.shared.view.SelectDateDialog
import ru.rescqd.jetschedule.ui.screen.schedule.shared.view.ViewPairItems
import ru.rescqd.jetschedule.ui.view.ViewLoading
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleGroupScreen(
    modifier: Modifier,
    viewModel: ScheduleGroupViewModel,
    payload: LeftNavigationDrawerItemPayload.GroupPayload,
) {
    val state = viewModel.state.collectAsState()
    JetscheduleScaffold(
        modifier = modifier,
        topBar = {
            JetscheduleTopBar(
                modifier = modifier, title = R.string.top_bar_schedule_group
            )
        },
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state.value) {
                is ScheduleGroupViewState.Display -> ViewDisplay(modifier = modifier,
                    state = currentState,
                    dateChange = { viewModel.obtainEvent(ScheduleGroupEvent.DateSelected(it)) },
                    onGroupClick = {},
                    onPairModelClick = { viewModel.obtainEvent(ScheduleGroupEvent.PairClicked(it)) },
                    gotoPrevDate = { viewModel.obtainEvent(ScheduleGroupEvent.BackPressed) })
                is ScheduleGroupViewState.Loading -> ViewLoading(modifier)
            }
        }
    }

    LaunchedEffect(key1 = state,
        key2 = payload,
        block = { viewModel.obtainEvent(ScheduleGroupEvent.EnterScreen(payload)) })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ViewDisplay(
    modifier: Modifier,
    state: ScheduleGroupViewState.Display,
    dateChange: (LocalDate) -> Unit,
    onGroupClick: () -> Unit,
    onPairModelClick: (Long) -> Unit,
    gotoPrevDate: () -> Unit,
) {
    state.prevDate?.let { BackHandler { gotoPrevDate.invoke() } }
    val coroutineScope = rememberCoroutineScope()
    val items = state.pairCardModelsFlow.collectAsState(initial = emptyList())
    val dialogState = rememberMaterialDialogState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val availableDates by state.synchronizedDates.collectAsState(initial = emptyList())
    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    ModalBottomSheetLayout(modifier = modifier,
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetShape = MaterialTheme.shapes.large,
        scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = .3f),
        sheetContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.onBackground),
        sheetContent = {
            SheetContent(
                pairMoreInfo = state.pairMoreInfoModel, dateChange
            )
        }) { //TODO(dateChange)
        SelectDateDialog(
            dialogState = dialogState,
            date = state.date,
            dateChange = dateChange,
            availableDates = availableDates
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            GroupContent(group = state.groupName, onGroupClick = onGroupClick)
            Divider(modifier = modifier.fillMaxWidth())
            WeekCalendar(
                selectionDateFlow = state.synchronizedDates,
                selectionDate = state.date,
                onClick = dateChange
            )
            ViewPairItems(
                items = items.value
            ) { pairId ->
                coroutineScope.launch {
                    if (state.pairMoreInfoModel == null) {
                        onPairModelClick.invoke(pairId)
                        sheetState.show()
                    } else {
                        if (state.pairMoreInfoModel.id == pairId) {
                            if (sheetState.isVisible) {
                                sheetState.hide()
                            } else {
                                sheetState.show()
                            }
                        } else {
                            if (sheetState.isVisible) {
                                sheetState.hide()
                            }
                            onPairModelClick.invoke(pairId)
                            sheetState.show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupContent(
    group: String,
    onGroupClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier
                .padding(5.dp)
                .clickable { onGroupClick.invoke() },
            text = group,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SheetContent(pairMoreInfo: PairMoreInfoModelGroup?, onDateClick: (LocalDate) -> Unit) {
    val configuration = LocalConfiguration.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(
                (configuration.screenHeightDp * 0.25).dp, (configuration.screenHeightDp * 0.50).dp
            )
            .padding(horizontal = 5.dp)
            .padding(top = 56.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (pairMoreInfo == null) {
                ViewLoading()
            } else {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 5.dp)
                        .height(5.dp),
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = stringResource(id = R.string.schedule_group_bottom_sheet_title),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    BottomSheetKeyValue(Key = {
                        Text(stringResource(id = R.string.bottom_sheet_date))
                    }, Value = {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 2.dp, horizontal = 3.dp)
                                .clickable { onDateClick.invoke(pairMoreInfo.date) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                                text = pairMoreInfo.date.toPrettyString,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    })
                    BottomSheetKeyValue(Key = {
                        Text(stringResource(id = R.string.bottom_sheet_pair_order))
                    }, Value = {
                        Text(pairMoreInfo.pairOrder.toString())
                    })

                    BottomSheetKeyValue(Key = {
                        Text(stringResource(id = R.string.bottom_sheet_subject))
                    }, Value = {
                        Text(pairMoreInfo.subject)
                    })
                    BottomSheetKeyValue(Key = {
                        Text(stringResource(id = R.string.bottom_sheet_teacher_fio))
                    }, Value = {
                        Text(pairMoreInfo.teacher)
                    })
                    BottomSheetKeyValue(Key = {
                        Text(stringResource(id = R.string.bottom_sheet_audience))
                    }, Value = {
                        Text(pairMoreInfo.audience)
                    })
                    val nextDates by pairMoreInfo.nextDays.collectAsState(initial = emptyList())
                    BottomSheetDateItem(
                        Key = {
                            Text(stringResource(id = R.string.bottom_sheet_next_days))
                        }, dates = nextDates, onDateClick = onDateClick
                    )
                    val prevDates by pairMoreInfo.prevDays.collectAsState(initial = emptyList())
                    BottomSheetDateItem(
                        Key = {
                            Text(stringResource(id = R.string.bottom_sheet_prev_days))
                        }, dates = prevDates, onDateClick = onDateClick
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomSheetKeyValue(
    Key: @Composable () -> Unit,
    Value: @Composable () -> Unit,

    ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Key()
        Value()
    }
}


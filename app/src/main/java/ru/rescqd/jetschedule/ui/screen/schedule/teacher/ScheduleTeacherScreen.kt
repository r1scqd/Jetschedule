package ru.rescqd.jetschedule.ui.screen.schedule.teacher

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemPayload
import ru.rescqd.jetschedule.ui.screen.schedule.shared.view.BottomSheetDateItem
import ru.rescqd.jetschedule.ui.screen.schedule.shared.view.SelectDateDialog
import ru.rescqd.jetschedule.ui.screen.schedule.shared.view.ViewPairItems
import ru.rescqd.jetschedule.ui.screen.schedule.teacher.model.PairMoreInfoModelTeacher
import ru.rescqd.jetschedule.ui.screen.schedule.teacher.model.ScheduleTeacherEvent
import ru.rescqd.jetschedule.ui.screen.schedule.teacher.model.ScheduleTeacherViewState
import ru.rescqd.jetschedule.ui.view.ViewLoading
import ru.rescqd.jetscheduleo.ui.components.NiaOutlinedButton
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleTeacherScreen(
    modifier: Modifier,
    viewModel: ScheduleTeacherViewModel,
    payload: LeftNavigationDrawerItemPayload.TeacherPayload,
) {
    val state = viewModel.state.collectAsState()
    JetscheduleScaffold(
        modifier = modifier,
        topBar = {
            JetscheduleTopBar(modifier = modifier, title = R.string.top_bar_schedule_teacher)
        },
    ) { paddingValues ->
        Box(modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when (val currentState = state.value) {
                is ScheduleTeacherViewState.Display -> ViewDisplay(modifier = modifier,
                    state = currentState,
                    dateChange = { viewModel.obtainEvent(ScheduleTeacherEvent.DateSelected(it)) },
                    onTeacherClick = {},
                    onPairModelClick = { viewModel.obtainEvent(ScheduleTeacherEvent.PairClicked(it)) })
                is ScheduleTeacherViewState.Loading -> ViewLoading(modifier)
            }
        }
    }

    LaunchedEffect(key1 = state,
        key2 = payload,
        block = { viewModel.obtainEvent(ScheduleTeacherEvent.EnterScreen(payload)) })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ViewDisplay(
    modifier: Modifier, state: ScheduleTeacherViewState.Display,
    dateChange: (LocalDate) -> Unit,
    onTeacherClick: () -> Unit,
    onPairModelClick: (Long) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val items = state.pairCardModelsFlow.collectAsState(initial = emptyList())
    val dialogState = rememberMaterialDialogState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }
    ModalBottomSheetLayout(modifier = modifier,
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetShape = MaterialTheme.shapes.large,
        scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = .3f),
        sheetContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.onBackground),

        sheetContent = { SheetContent(pairMoreInfo = state.pairMoreInfoModelFlow) }) {
        val synchronizedDates by state.synchronizedDates.collectAsState(initial = emptyList())
        SelectDateDialog(dialogState = dialogState,
            date = state.date,
            dateChange = dateChange,
            synchronizedDates)
        Column(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)) {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(modifier = modifier
                    .padding(5.dp)
                    .clickable { onTeacherClick.invoke() },
                    text = state.teacherName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center)
            }
            Divider(modifier = modifier.fillMaxWidth())
            NiaOutlinedButton(
                modifier = modifier.fillMaxWidth(),
                onClick = { dialogState.show() },
                text = {
                    Text(modifier = modifier,
                        text = "${stringResource(id = R.string.custom_select_outline_button_date)}: ${state.date}")
                },
            )
            ViewPairItems(items = items.value) { pairId ->
                coroutineScope.launch {
                    if (state.pairMoreInfoModelFlow == null) {
                        onPairModelClick.invoke(pairId)
                        sheetState.show()
                    } else {
                        if (state.pairMoreInfoModelFlow.id == pairId) {
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
private fun SheetContent(pairMoreInfo: PairMoreInfoModelTeacher?) {
    val configuration = LocalConfiguration.current
    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn((configuration.screenHeightDp * 0.25).dp,
            (configuration.screenHeightDp * 0.75).dp)
        .padding(horizontal = 5.dp)
        .padding(top = 10.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                Text(text = stringResource(id = R.string.bottom_sheet_title),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
                Column(Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)) {
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
                    BottomSheetDateItem(Key = {
                        Text(stringResource(id = R.string.bottom_sheet_next_days))
                    }, dates = nextDates)
                    val prevDates by pairMoreInfo.prevDays.collectAsState(initial = emptyList())
                    BottomSheetDateItem(Key = {
                        Text(stringResource(id = R.string.bottom_sheet_prev_days))
                    }, dates = prevDates)
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
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Key()
        Value()
    }
}

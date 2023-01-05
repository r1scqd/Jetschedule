package ru.rescqd.jetschedule.ui.screen.schedule.schedule

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.CustomSelectScreen
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.CustomSelectViewModel
import ru.rescqd.jetschedule.ui.screen.schedule.group.ScheduleGroupScreen
import ru.rescqd.jetschedule.ui.screen.schedule.group.ScheduleGroupViewModel
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.AddDrawerScreen
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.AddDrawerViewModel
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.*
import ru.rescqd.jetschedule.ui.screen.schedule.teacher.ScheduleTeacherScreen
import ru.rescqd.jetschedule.ui.screen.schedule.teacher.ScheduleTeacherViewModel
import ru.rescqd.jetschedule.ui.view.ViewLoading

@Composable
fun MainScheduleScreen(
    modifier: Modifier,
    viewModel: MainScheduleViewModel,
) {
    val state = viewModel.state.collectAsState()
    when (val currentState = state.value) {
        is ScheduleViewState.Display -> ViewDisplay(
            state = currentState,
            changeItem = { viewModel.obtainEvent(ScheduleEvent.LeftNavigationDrawerItemChanged(it)) },
            removeItem = { viewModel.obtainEvent(ScheduleEvent.LeftNavigationDrawerItemDelete(it)) },
            modifier = modifier,
            addItem = { viewModel.obtainEvent(ScheduleEvent.LeftNavigationDrawerItemAdd(it)) }
        )
        is ScheduleViewState.Loading -> ViewLoading(modifier)
    }

    LaunchedEffect(key1 = state, block = {
        viewModel.obtainEvent(ScheduleEvent.EnterScreen)
    })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun ViewDisplay(
    state: ScheduleViewState.Display,
    changeItem: (LeftNavigationDrawerItem) -> Unit,
    removeItem: (LeftNavigationDrawerItem) -> Unit,
    addItem: (LeftNavigationDrawerItem) -> Unit,
    modifier: Modifier,
) {
    var addDrawerDialogState by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    if (addDrawerDialogState) {
        AddDrawerDialog(
            modifier = modifier,
            closeDialog = { addDrawerDialogState = false },
            addItem = addItem
        )
    }

    BackHandler(drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }

    DismissibleNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet {
                Text(
                    text = stringResource(id = R.string.schedule_left_navigation_drawer_title),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                LazyColumn {
                    items(state.items.toList()) {
                        NavigationDrawerItem(label = { Text(text = it.displayName) },
                            selected = state.currentItem == it,
                            onClick = { changeItem.invoke(it) },
                            icon = {
                                if (it.deletable)
                                    Icon(imageVector = Icons.Outlined.Remove,
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            removeItem.invoke(it)
                                        })
                            }
                        )
                    }
                }
                NavigationDrawerItem(label = { Text(text = stringResource(id = R.string.schedule_left_navigation_drawer_add)) },
                    selected = false,
                    onClick = { addDrawerDialogState = true })
            }
        }
    ) {
        when (state.currentItem.itemType) {
            LeftNavigationDrawerItemType.GROUP -> GroupContent(modifier,
                state.currentItem.itemPayload as LeftNavigationDrawerItemPayload.GroupPayload)
            LeftNavigationDrawerItemType.TEACHER -> TeacherContent(modifier = modifier,
                state.currentItem.itemPayload as LeftNavigationDrawerItemPayload.TeacherPayload)
            LeftNavigationDrawerItemType.CUSTOM_SELECT -> CustomSelectContent(modifier = modifier)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDrawerDialog(
    modifier: Modifier,
    closeDialog: () -> Unit,
    addItem: (LeftNavigationDrawerItem) -> Unit,
) {
    val vm = hiltViewModel<AddDrawerViewModel>()
    Dialog(onDismissRequest = {
        closeDialog.invoke()
        vm.resetState()
    }) {
        Scaffold(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(.8f)
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.large)
                .border(1.dp,
                    MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.large),
        ) {
            Box(modifier.padding(it)) {
                AddDrawerScreen(modifier = modifier,
                    viewModel = vm,
                    closeDialog) { label, type, payload ->
                    addItem.invoke(LeftNavigationDrawerItem(label, type, payload))
                }
            }
        }
    }
}

@Composable
fun GroupContent(
    modifier: Modifier,
    groupPayload: LeftNavigationDrawerItemPayload.GroupPayload,
) {
    val vm = hiltViewModel<ScheduleGroupViewModel>()
    ScheduleGroupScreen(modifier = modifier, viewModel = vm, payload = groupPayload)
}

@Composable
fun TeacherContent(
    modifier: Modifier,
    teacherPayload: LeftNavigationDrawerItemPayload.TeacherPayload,
) {
    val vm = hiltViewModel<ScheduleTeacherViewModel>()
    ScheduleTeacherScreen(modifier = modifier, viewModel = vm, payload = teacherPayload)
}

@ExperimentalSerializationApi
@Composable
private fun CustomSelectContent(
    modifier: Modifier,
) {
    val vm = hiltViewModel<CustomSelectViewModel>()
    CustomSelectScreen(modifier = modifier, viewModel = vm)
}
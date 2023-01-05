package ru.rescqd.jetschedule.ui.screen.settings.behavior

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.theme.ScheduleBackendProvider
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.JetscheduleSettingSwitch
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.components.NiaDropdownMenuButton
import ru.rescqd.jetschedule.ui.home.HomeSections
import ru.rescqd.jetschedule.ui.screen.settings.appearance.view.CustomDropdownMenu
import ru.rescqd.jetschedule.ui.screen.settings.appearance.view.CustomSwitch
import ru.rescqd.jetschedule.ui.screen.settings.behavior.model.SettingsBehaviorViewState
import ru.rescqd.jetschedule.ui.screen.settings.behavior.model.SettingsContainer
import ru.rescqd.jetschedule.ui.view.ViewLoading
import ru.rescqd.jetscheduleo.ui.components.NiaOutlinedButton
import java.time.Duration


val repeatTimeItems = listOf(
    Duration.ofHours(1),
    Duration.ofHours(2),
    Duration.ofHours(5),
    Duration.ofHours(12),
    Duration.ofHours(24),
)
val flexTimeItems = listOf(
    Duration.ofMinutes(15),
    Duration.ofMinutes(30),
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBehaviorScreen(
    modifier: Modifier, viewModel: SettingsBehaviorViewModel, upPress: () -> Unit
) {

    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state, block = { viewModel.enterScreen() })

    JetscheduleScaffold(modifier = modifier, topBar = {
        JetscheduleTopBar(
            modifier = modifier, title = R.string.top_bar_behavior, upPress = upPress
        )
    }, content = {
        Box(
            modifier = modifier.padding(it)
        ) {
            when (val currentState = state.value) {
                is SettingsBehaviorViewState.Display -> ViewDisplay(
                    modifier, currentState, viewModel
                )
                is SettingsBehaviorViewState.Loading -> ViewLoading(modifier)
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewDisplay(
    modifier: Modifier = Modifier,
    state: SettingsBehaviorViewState.Display,
    viewModel: SettingsBehaviorViewModel
) {
    var backendProvider by remember {
        mutableStateOf(state.backendProvider)
    }

    var isWorkManagerEnable by remember {
        mutableStateOf(state.isPeriodicWorkEnabled)
    }
    var flexTime by remember {
        mutableStateOf(state.flexInterval)
    }
    var repeatTime by remember {
        mutableStateOf(state.repeatInterval)
    }
    var dateThreshold by remember {
        mutableStateOf(state.dateThreshold.toString())
    }
    var dateOffset by remember {
        mutableStateOf(state.dateOffset.toString())
    }
    var startDestination by remember {
        mutableStateOf(state.startDestination)
    }
    JetscheduleScaffold(modifier = modifier, bottomBar = {
        NiaOutlinedButton(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                viewModel.confirmSettings(
                    SettingsContainer(
                        backendProvider,
                        isWorkManagerEnable,
                        repeatTime,
                        flexTime,
                        dateThreshold,
                        dateOffset,
                        startDestination
                    )
                )
            },
            text = { Text(text = stringResource(id = R.string.button_action_confirm)) },
        )
    }) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
           //JetscheduleSettingMenu(
            //                modifier = modifier,
            //                items = HomeSections.values().toList(),
            //                title = stringResource(scheduleUid = R.string.behavior_start_dest_title),
            //                itemText = { Text(text = stringResource(scheduleUid = it.title), style = MaterialTheme.typography.labelMedium) },
            //                selectedItem = startDestination,
            //                onItemClick = { startDestination = it },
            //            )
            CustomDropdownMenu(
                modifier = modifier,
                icon = Icons.Outlined.Restore,
                title = R.string.behavior_start_dest_title,
                items = HomeSections.values().toList(),
                onItemClick = { startDestination = it },
                text = { Text(text = stringResource(id = startDestination.title)) }
            ) { Text(text = stringResource(id = it.title)) }
            CustomDropdownMenu(
                modifier = modifier,
                icon = Icons.Outlined.SelfImprovement,
                title = R.string.appearance_screen_backend_provider,
                items = ScheduleBackendProvider.values().toList(),
                onItemClick = { backendProvider = it },
                text = { Text(text = stringResource(id = backendProvider.displayName)) }
            ) { Text(text = stringResource(id = it.displayName)) }
            JetscheduleSettingSwitch(checked = isWorkManagerEnable,
                onCheckedChange = {isWorkManagerEnable = it},
                title = stringResource(id = R.string.behavior_periodic_work_title),
            )
            CustomSwitch(modifier = modifier,
                icon = Icons.Outlined.Work,
                title = R.string.behavior_periodic_work_title,
                checked = isWorkManagerEnable,
                onClick = { isWorkManagerEnable = it })
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.behavior_periodic_work_repeat_interval))
                NiaDropdownMenuButton(
                    modifier = modifier.width(128.dp),
                    items = repeatTimeItems,
                    onItemClick = { repeatTime = it },
                    text = {
                        Text(
                            modifier = modifier,
                            text = "${repeatTime.toHours()} ${stringResource(id = R.string.hours)}"
                        )
                    },
                    itemText = {
                        Text(
                            modifier = modifier,
                            text = "${it.toHours()} ${stringResource(id = R.string.hours)}"
                        )
                    },
                    enabled = isWorkManagerEnable
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.behavior_periodic_work_flex_interval))
                NiaDropdownMenuButton(
                    modifier = modifier.width(128.dp),
                    items = flexTimeItems,
                    onItemClick = { flexTime = it },
                    text = {
                        Text(
                            modifier = modifier,
                            text = "${flexTime.toMinutes()} ${stringResource(id = R.string.minutes)}"
                        )
                    },
                    itemText = {
                        Text(
                            modifier = modifier,
                            text = "${it.toMinutes()} ${stringResource(id = R.string.minutes)}"
                        )
                    },
                    enabled = isWorkManagerEnable,
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.behavior_periodic_work_date_threshold))
                OutlinedTextField(
                    modifier = modifier.width(128.dp),
                    value = dateThreshold,
                    onValueChange = { dateThreshold = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    enabled = isWorkManagerEnable,
                    shape = MaterialTheme.shapes.medium,
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.behavior_periodic_work_date_offset))
                OutlinedTextField(
                    modifier = modifier.width(128.dp),
                    value = dateOffset,
                    onValueChange = { dateOffset = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    enabled = isWorkManagerEnable
                )
            }
        }
    }
}

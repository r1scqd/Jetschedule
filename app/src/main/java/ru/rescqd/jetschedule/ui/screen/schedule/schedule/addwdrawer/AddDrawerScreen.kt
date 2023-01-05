package ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model.AddDrawerEvent
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model.AddDrawerViewState
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.view.GroupSelectView
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.view.SelectTypeView
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.view.TeacherSelectView
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemPayload
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItemType
import ru.rescqd.jetschedule.ui.view.ViewLoading
import ru.rescqd.jetscheduleo.ui.components.NiaTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDrawerScreen(
    modifier: Modifier,
    viewModel: AddDrawerViewModel,
    cancelPress: () -> Unit,
    addItemInfo: (String, LeftNavigationDrawerItemType, LeftNavigationDrawerItemPayload) -> Unit,
) {

    BackHandler {
        cancelPress.invoke()
        viewModel.resetState()
    }
    val state = viewModel.state.collectAsState()
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Row(modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NiaTextButton(
                    onClick = {
                        cancelPress.invoke()
                        viewModel.resetState()
                    }, text = {
                        Text(text = stringResource(
                            id = R.string.button_action_cancel),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    })
                Divider(modifier.width(5.dp))
                val currentState = state.value
                val confirmEnable =
                    currentState is AddDrawerViewState.DisplayTeacherSelect || currentState is AddDrawerViewState.DisplayGroupSelect
                NiaTextButton(
                    onClick = { viewModel.obtainEvent(AddDrawerEvent.ConfirmClicked) }, text = {
                        Text(text = stringResource(
                            id = R.string.button_action_confirm),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary)
                    },
                    enabled = confirmEnable
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = modifier
            .fillMaxSize()
            .padding(paddingValues))
        when (val currentState = state.value) {
            is AddDrawerViewState.DisplaySelectType -> SelectTypeView(modifier) {
                viewModel.obtainEvent(
                    AddDrawerEvent.ItemTypeSelected(it),
                )
            }
            is AddDrawerViewState.DisplayTeacherSelect -> TeacherSelectView(modifier,
                currentState) { viewModel.obtainEvent(AddDrawerEvent.TeacherSelected(it)) }
            is AddDrawerViewState.DisplayGroupSelect -> GroupSelectView(modifier,
                currentState) { viewModel.obtainEvent(AddDrawerEvent.GroupSelected(it)) }
            is AddDrawerViewState.Loading -> ViewLoading(modifier)
            is AddDrawerViewState.DialogConfirmExit -> {
                addItemInfo(currentState.label, currentState.type, currentState.payload)
                viewModel.resetState()
                cancelPress.invoke()
            }
        }
    }
    LaunchedEffect(key1 = state,
        key2 = addItemInfo,
        block = {
            viewModel.obtainEvent(AddDrawerEvent.EnterScreen(addItemInfo))
        })
}
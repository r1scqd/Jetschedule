package ru.rescqd.jetschedule.ui.screen.schedule.manager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.view.ViewError
import ru.rescqd.jetschedule.ui.screen.schedule.manager.model.ScheduleManagerViewState
import ru.rescqd.jetschedule.ui.screen.schedule.manager.view.ViewDisplay
import ru.rescqd.jetschedule.ui.view.ViewLoading
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleManagerScreen (
    modifier: Modifier,
    viewModel: ScheduleManagerViewModel
){
    val state = viewModel.state.collectAsState()
    JetscheduleScaffold(
        modifier = modifier,
        topBar = { JetscheduleTopBar(modifier = modifier, title = R.string.top_bar_manager) },
        content = {
            Box(
                modifier
                    .fillMaxSize()
                    .padding(it)){
                when(val currentState = state.value){
                    is ScheduleManagerViewState.Display ->  ViewDisplay(modifier = modifier, onDateClick = viewModel::dateClick, state = currentState)
                    is ScheduleManagerViewState.Error -> ViewError(msg = currentState.cause)
                    is ScheduleManagerViewState.Loading -> ViewLoading()
                }
            }
        }
    )
    LaunchedEffect(key1 = state, block = {
        viewModel.enterScreen()
    })
}
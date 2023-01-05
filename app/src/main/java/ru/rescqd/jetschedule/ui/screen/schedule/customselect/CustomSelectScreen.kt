package ru.rescqd.jetschedule.ui.screen.schedule.customselect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.model.CustomSelectViewState
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.view.ViewDisplayAudience
import ru.rescqd.jetschedule.ui.screen.schedule.customselect.view.ViewError
import ru.rescqd.jetschedule.ui.view.ViewLoading

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun CustomSelectScreen(
    modifier: Modifier,
    viewModel: CustomSelectViewModel,
){
    val state = viewModel.state.collectAsState()

    JetscheduleScaffold(
        modifier = modifier,
        topBar = { JetscheduleTopBar(modifier = modifier, title = R.string.top_bar_custom_select) },
        content = {
            Box(
                modifier
                    .fillMaxSize()
                    .padding(it)){
                when (val currentState = state.value){
                    is CustomSelectViewState.DisplayAudience ->
                        ViewDisplayAudience(
                            modifier,
                            currentState,
                            viewModel::filterCLicked,
                            viewModel::dateClicked,
                            viewModel::pairOrderClicked,
                            viewModel::corpusSelected,
                        )
                    is CustomSelectViewState.Error -> ViewError(msg = currentState.message)
                    is CustomSelectViewState.Loading -> ViewLoading(modifier)
                }
            }
        }
    )
    LaunchedEffect(key1 = state, block = {viewModel.enterScreen()})
}
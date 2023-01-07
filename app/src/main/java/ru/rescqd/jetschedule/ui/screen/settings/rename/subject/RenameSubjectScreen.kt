package ru.rescqd.jetschedule.ui.screen.settings.rename.subject

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.AutocompleteTextField
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model.RenameSubjectError
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model.RenameSubjectEvent
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model.RenameSubjectViewState
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model.SubjectModel
import ru.rescqd.jetschedule.ui.view.ViewLoading
import ru.rescqd.jetscheduleo.ui.components.NiaOutlinedButton


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RenameSubjectScreen(
    modifier: Modifier,
    viewModel: RenameSubjectViewModel,
    upPress: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(key1 = state, block = { viewModel.obtainEvent(RenameSubjectEvent.EnterScreen) })
    val kbd = LocalSoftwareKeyboardController.current
    JetscheduleScaffold(modifier = modifier, topBar = {
        JetscheduleTopBar(
            modifier = modifier,
            title = R.string.top_bar_rename_subject,
            upPress = { upPress.invoke(); kbd?.hide() }
        )
    }) { paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state.value) {
                is RenameSubjectViewState.DisplaySubjectSelect -> ViewDisplay(
                    modifier = modifier,
                    state = currentState,
                    subjectSelect = { viewModel.obtainEvent(RenameSubjectEvent.SubjectSelected(it)) },
                    displayNameChange = {
                        viewModel.obtainEvent(
                            RenameSubjectEvent.SubjectDisplayNameChange(
                                it
                            )
                        )
                    },
                    confirmClick = { viewModel.obtainEvent(RenameSubjectEvent.ConfirmClicked) }
                )
                is RenameSubjectViewState.Loading -> ViewLoading(modifier)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ViewDisplay(
    modifier: Modifier,
    state: RenameSubjectViewState.DisplaySubjectSelect,
    subjectSelect: (SubjectModel) -> Unit,
    displayNameChange: (String) -> Unit,
    confirmClick: () -> Unit
) {
    var autocompleteText by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        AutocompleteTextField(
            modifier = Modifier.fillMaxWidth(),
            itemSelected = subjectSelect,
            cleared = { autocompleteText = "" },
            valueChanged = { autocompleteText = it },
            subjects = state.subjects,
            valueFromItem = { it.name },
            label = R.string.rename_subject_select_subject,
            autocompleteItemContent = {
                Text(
                    text = "${it.name} ${if (it.name == it.displayName) "" else "(${it.displayName})"}",
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )
        state.currentSubject?.let {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = it.displayName, onValueChange = displayNameChange, label = {
                    Text(
                        text = stringResource(
                            id = R.string.rename_subject_display_subject_label,
                        ),
                        style = MaterialTheme.typography.labelMedium
                    )
                })
            NiaOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = confirmClick,
                text = { Text(text = stringResource(id = R.string.button_action_confirm)) })
        }

        state.sendingError?.let {
            ViewErrorMsg(error = it)
        }
    }
}

@Composable
private fun ViewErrorMsg(
    error: RenameSubjectError
) {
    when (error) {
        RenameSubjectError.SubjectNotSelected -> Text(
            text = stringResource(id = R.string.rename_subject_select_subject_error),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        RenameSubjectError.Success -> Text(
            text = stringResource(id = R.string.rename_subject_success),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
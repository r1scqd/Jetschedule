package ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.AutocompleteTextField
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model.AddDrawerViewState
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model.GroupModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun GroupSelectView(
    modifier: Modifier,
    state: AddDrawerViewState.DisplayGroupSelect,
    groupSelect: (GroupModel) -> Unit,
) {
    val text = remember { mutableStateOf("") }
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if (state.groups.isEmpty()) {
                Text(text = stringResource(id = R.string.add_drawer_select_group_no_items),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                )
            } else {
                Text(text = stringResource(id = R.string.add_drawer_select_group),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge)
                AutocompleteTextField(modifier = modifier.padding(horizontal = 10.dp),
                    itemSelected = groupSelect,
                    cleared = { text.value = "" },
                    valueChanged = { text.value = it },
                    subjects = state.groups,
                    valueFromItem = { it.name },
                    label = R.string.add_drawer_select_group_autocomplete_label,
                    autocompleteItemContent = {
                        Text(text = it.name,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp))
                    }
                )
            }
        }
    }
}
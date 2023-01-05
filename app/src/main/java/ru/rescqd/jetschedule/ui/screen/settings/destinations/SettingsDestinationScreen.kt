package ru.rescqd.jetschedule.ui.screen.settings.destinations

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleCard
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.screen.settings.destinations.model.DestinationModel
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDestinationScreen(
    modifier: Modifier,
    navController: NavController,
    onManualScreenOpen: () -> Unit,
) {
    JetscheduleScaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            JetscheduleTopBar(
                modifier = modifier, title = R.string.top_bar_settings
            )
        },
        content = { Body(modifier = modifier, navController = navController, paddingValues = it) },
        bottomBar = { Bottom(modifier = modifier, onManualScreenOpen = onManualScreenOpen) })

}

@Stable
@Composable
private fun Bottom(
    modifier: Modifier, onManualScreenOpen: () -> Unit
) {
    Box(modifier = modifier.padding(horizontal = 10.dp, vertical = 15.dp)) {
        JetscheduleCard(
            modifier = modifier.clickable { onManualScreenOpen.invoke() },
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.settings_screen_open_manual),
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    modifier = modifier,
                    contentDescription = null,
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            }
        }
    }
}

@Stable
@Composable
private fun Body(
    modifier: Modifier, navController: NavController, paddingValues: PaddingValues
) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        items(DestinationModel.values()) { dest ->
            DestinationItem(modifier = modifier, navController = navController, dest = dest)
            Divider(
                modifier
                    .fillMaxWidth()
                    .height(10.dp), color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Stable
@Composable
private fun DestinationItem(
    modifier: Modifier,
    navController: NavController,
    dest: DestinationModel,
) {
    JetscheduleCard(
        modifier = modifier.clickable { navController.navigate(dest.route) },
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dest.icon?.let {
                Icon(
                    modifier = modifier,
                    imageVector = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            }
            Text(
                text = stringResource(id = dest.resId), style = MaterialTheme.typography.titleLarge
            )
            Icon(
                modifier = modifier,
                contentDescription = null,
                imageVector = Icons.Filled.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.surfaceTint
            )
        }
    }
}
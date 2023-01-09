package ru.rescqd.jetschedule.ui.screen.settings.destinations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.screen.settings.destinations.model.DestinationModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDestinationScreen(
    modifier: Modifier,
    navController: NavController,
    onManualScreenOpen: () -> Unit,
) {
    JetscheduleScaffold(modifier = modifier.fillMaxSize(), topBar = {
        JetscheduleTopBar(
            modifier = modifier, title = R.string.top_bar_settings
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Content(modifier = Modifier, navController = navController)
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    navController: NavController,
) {
    Column(modifier.fillMaxSize()) {
        SettingsItemsContent(navController = navController)
    }
}

@Composable
private fun SettingsItemsContent(
    navController: NavController,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 5.dp, horizontal = 10.dp)
    ) {
        items(DestinationModel.values()) { destination ->
            DestItem(
                navController = navController,
                destination = destination,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DestItem(
    modifier: Modifier = Modifier,
    navController: NavController,
    destination: DestinationModel,
) {
    ElevatedCard(
        onClick = { navController.navigate(destination.route) },
        modifier = modifier.height(72.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                destination.icon?.let {
                    Icon(
                        it,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    )
                }
            }
            Column(Modifier.weight(5f)) {
                Text(
                    text = stringResource(id = destination.titleId),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                destination.subtitleId?.let {
                    Text(
                        text = stringResource(id = it),
                        style = MaterialTheme.typography.labelLarge,
                        color = LocalContentColor.current.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

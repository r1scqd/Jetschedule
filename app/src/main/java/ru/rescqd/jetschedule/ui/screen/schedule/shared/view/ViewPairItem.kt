package ru.rescqd.jetschedule.ui.screen.schedule.shared.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.rescqd.jetschedule.ui.screen.schedule.shared.model.PairCardModel

@Composable
fun ViewPairItems(
    modifier: Modifier = Modifier,
    items: List<PairCardModel>,
    onItemClick: (Long) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)) {
        items(items) {
            Box(modifier = modifier.padding(vertical = 8.dp)) {
                OutlinedCard(
                    modifier = modifier
                        .clickable { onItemClick.invoke(it.id) }
                        .fillMaxWidth(),
                ) {
                    Row(modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = modifier
                            .weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = it.pairOrder.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Column(modifier = modifier
                            .weight(5f),
                            horizontalAlignment = Alignment.Start) {
                            Text(modifier = modifier,
                                text = it.subject,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(modifier = modifier,
                                text = it.teacher,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Column(modifier = modifier
                            .weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            OutlinedCard(modifier = modifier) {
                                Box(modifier = modifier.padding(2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = it.audience)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
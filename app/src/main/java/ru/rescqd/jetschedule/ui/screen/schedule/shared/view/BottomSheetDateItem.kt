package ru.rescqd.jetschedule.ui.screen.schedule.shared.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.rescqd.jetschedule.ui.components.JetscheduleCard
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDateItem(
    Key: @Composable () -> Unit,
    dates: List<LocalDate>,
    onDateClick: (LocalDate) -> Unit = {},
) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Key()
        LazyRow(Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
            items(dates) { date ->

                OutlinedCard(
                    onClick = { onDateClick.invoke(date) },
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                        text = "${
                            date.month.getDisplayName(
                                TextStyle.SHORT,
                                Locale.getDefault()
                            )
                        } ${date.dayOfMonth}",
                    )
                }
            }
        }
    }
}
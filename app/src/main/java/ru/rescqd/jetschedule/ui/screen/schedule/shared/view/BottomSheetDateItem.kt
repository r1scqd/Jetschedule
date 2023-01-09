package ru.rescqd.jetschedule.ui.screen.schedule.shared.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.calendar.clickable
import ru.rescqd.jetschedule.ui.screen.schedule.shared.toPrettyString
import java.time.LocalDate

@Composable
fun BottomSheetDateItem(
    Key: @Composable () -> Unit,
    dates: List<LocalDate>,
    onDateClick: (LocalDate) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Key()
        if (dates.isEmpty())
            Text(text = stringResource(id = R.string.bottom_sheet_dates_not_found))
        else
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                items(dates) { date ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 3.dp)
                            .clickable { onDateClick.invoke(date) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                            text = date.toPrettyString,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
    }
}
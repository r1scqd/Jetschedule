package ru.rescqd.jetschedule.ui.screen.schedule.shared.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import ru.rescqd.jetschedule.R
import java.time.LocalDate

@Composable
fun SelectDateDialog(
    dialogState: MaterialDialogState,
    date: LocalDate,
    dateChange: (LocalDate) -> Unit,
    availableDates: List<LocalDate> = emptyList()
) {
    MaterialDialog(dialogState = dialogState,
        buttons = {
            positiveButton(res = R.string.button_action_confirm,
                textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.tertiary))
            negativeButton(res = R.string.button_action_cancel,
                textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.tertiary))
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium) {
        datepicker(initialDate = date,
            title = stringResource(id = R.string.custom_select_date_picker_title),
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.background,
                headerTextColor = MaterialTheme.colorScheme.primary,
                calendarHeaderTextColor = MaterialTheme.colorScheme.secondary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                dateActiveTextColor = MaterialTheme.colorScheme.primary,
                dateInactiveBackgroundColor = Color.Transparent,
                dateInactiveTextColor = MaterialTheme.colorScheme.secondary,
            ),
            onDateChange = dateChange,
            allowedDateValidator = {availableDates.contains(it)}
        )
    }
}
package ru.rescqd.jetschedule.ui.screen.manual

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetscheduleo.ui.components.NiaOutlinedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualScreen(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    viewModel: ManualViewModel
) {
    val lazy = rememberLazyListState()
    JetscheduleScaffold(
        modifier = modifier,
        topBar = { JetscheduleTopBar(modifier = modifier, title = R.string.manual_screen_title) },
        content = {
            LazyColumn(modifier = modifier.padding(it), state = lazy) {
                item{
                    Text(
                        text = stringResource(id = R.string.manual_screen_text),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        },
        bottomBar = {
            NiaOutlinedButton(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    upPress.invoke()
                    viewModel.setIsFirstLaunch(false)
                },
                text = {Text(stringResource(id =R.string.manual_screen_exit), style = MaterialTheme.typography.titleLarge)}
            )
        }
    )
}

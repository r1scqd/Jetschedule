package ru.rescqd.jetschedule.ui.screen.settings.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutApplicationScreen(
    modifier: Modifier = Modifier,
    upPress: () -> Unit
) {
    JetscheduleScaffold(
        modifier = modifier,
        topBar = {
            JetscheduleTopBar(
                modifier = modifier,
                title = R.string.top_bar_about_application,
                upPress = upPress
            )
        },
        content = { Box(modifier = modifier.padding(it)){AboutContent(modifier) }}
    )
}

@Composable
private fun AboutContent(modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(modifier = modifier, text = stringResource(id = R.string.about_application_some_text))
    }
}
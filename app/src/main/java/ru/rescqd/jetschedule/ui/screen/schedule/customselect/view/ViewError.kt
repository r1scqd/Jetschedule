package ru.rescqd.jetschedule.ui.screen.schedule.customselect.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.rescqd.jetschedule.R

@Composable
fun ViewError(
    modifier: Modifier = Modifier,
    msg: String?
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = "${stringResource(id = R.string.error_base_title)} $msg")
    }
}
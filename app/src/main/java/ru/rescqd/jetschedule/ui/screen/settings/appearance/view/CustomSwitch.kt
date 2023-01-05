package ru.rescqd.jetschedule.ui.screen.settings.appearance.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomSwitch(
    modifier: Modifier,
    icon: ImageVector,
    @StringRes title: Int,
    checked: Boolean,
    onClick: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = modifier.width(30.dp),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceTint
        )

        Text(
            modifier = modifier.weight(5f),
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleMedium
        )

        Switch(
            modifier = modifier.weight(1f),
            checked = checked, onCheckedChange = onClick
        )
    }
}

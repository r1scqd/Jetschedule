package ru.rescqd.jetschedule.ui.screen.settings.appearance.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.rescqd.jetschedule.ui.components.NiaDropdownMenuButton

@Composable
fun <T> CustomDropdownMenu(
    modifier: Modifier,
    icon: ImageVector,
    @StringRes title: Int,
    items: List<T>,
    onItemClick: (item: T) -> Unit,
    text: @Composable () -> Unit,
    itemText: @Composable (item: T) -> Unit
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
        NiaDropdownMenuButton(
            modifier = modifier.wrapContentWidth(Alignment.End),
            items = items,
            onItemClick = onItemClick,
            text = text,
            itemText = itemText
        )
    }
}
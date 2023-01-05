package ru.rescqd.jetschedule.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.rescqd.jetschedule.R
import ru.rescqd.jetscheduleo.ui.components.NiaTextButton

//  modifier: Modifier,
//    icon: ImageVector? = null,
//    @StringRes title: Int,
//    items: List<T>,
//    onItemClick: (item: T) -> Unit,
//    enabled: Boolean = true,


@Composable
private fun JetscheduleSettingText(
    modifier: Modifier,
    title: String,
    hint: String?,
) {
    Column(modifier, verticalArrangement = Arrangement.Center) {
        Text(
            modifier = modifier.padding(bottom = 2.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
        )
        hint?.let {
            Text(
                modifier = modifier
                    .padding(top = 2.dp)
                    .fillMaxWidth(),
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun JetscheduleSettingText(
    modifier: Modifier,
    title: String,
    hint: (@Composable () -> Unit),
) {
    Column(modifier, verticalArrangement = Arrangement.Center) {
        Text(
            modifier = modifier.padding(bottom = 2.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
        )
        hint.invoke()
    }
}


@Composable
fun <T> JetscheduleSettingMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemClick: (T) -> Unit,
    enabled: Boolean = true,
    title: String,
    selectedItem: T,
    itemText: @Composable (item: T) -> Unit,
) {
    var showPopup by remember { mutableStateOf(false) }
    if (showPopup) {
        SettingPopupMenu(items = items,
            onItemClick = {
                onItemClick.invoke(it)
                showPopup = false
            },
            itemText = itemText,
            title = title,
            selectedItem = selectedItem,
            dismissCallback = { showPopup = false }
        )
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { if (enabled) showPopup = true },
        contentAlignment = Alignment.Center
    ) {
        Row(modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            JetscheduleSettingText(modifier = modifier,
                title = title,
                hint = { itemText.invoke(selectedItem) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> SettingPopupMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemClick: (T) -> Unit,
    itemText: @Composable (item: T) -> Unit,
    title: String,
    selectedItem: T,
    dismissCallback: () -> Unit,
) {
    var currentItem by remember {
        mutableStateOf(selectedItem)
    }
    Dialog(onDismissRequest = dismissCallback, properties = DialogProperties()) {
        Scaffold(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(.7f)
                .padding(horizontal = 32.dp)
                .clip(MaterialTheme.shapes.large)
                .border(1.dp, MaterialTheme.colorScheme.outline, shape = MaterialTheme.shapes.large),
            topBar = {
                Text(modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                    text = title,
                    style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            },
            bottomBar = {
                Row(modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NiaTextButton(
                        onClick = dismissCallback, text = {
                        Text(text = stringResource(
                            id = R.string.button_action_cancel),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    })
                    Divider(modifier.width(5.dp))
                    NiaTextButton(
                        onClick = { onItemClick.invoke(currentItem) }, text = {
                        Text(text = stringResource(
                            id = R.string.button_action_confirm),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary)
                    })
                }
            },
        ) {
            LazyColumn(modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(it)
            ) {
                items(items) {
                    Row(modifier = modifier
                        .fillMaxWidth()
                        .clickable { currentItem = it },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = modifier.padding(start = 15.dp)) {
                            RadioButton(modifier = modifier,
                                selected = currentItem == it,
                                onClick = { currentItem = it })
                        }
                        itemText.invoke(it)
                    }
                }
            }
        }
    }
}

@Composable
fun JetscheduleSettingSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    title: String,
    hint: String? = null,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange?.invoke(!checked) },
        contentAlignment = Alignment.Center
    ) {
        Row(modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            JetscheduleSettingText(modifier = modifier, title = title, hint = hint)
            Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
        }
    }
}
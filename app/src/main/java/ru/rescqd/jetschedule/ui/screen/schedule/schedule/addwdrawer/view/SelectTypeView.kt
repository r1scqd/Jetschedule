package ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.addwdrawer.model.AddDrawerItemType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTypeView(
    modifier: Modifier,
    typeSelect: (AddDrawerItemType) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Text(
                text = stringResource(id = R.string.add_drawer_select_type),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Divider(modifier.fillMaxWidth().height(1.dp))
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)) {
                items(AddDrawerItemType.values()) { item ->
                    Box(modifier
                        .fillMaxWidth()
                        .clickable { typeSelect.invoke(item) }) {
                        Text(
                            text = stringResource(id = item.displayName),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
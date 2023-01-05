package ru.rescqd.jetschedule.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@Stable
@Composable
fun JetscheduleTopBar(
    modifier: Modifier,
    @StringRes title: Int,
    upPress: (() -> Unit)? = null,
    optionPress: (() -> Unit)? = null,
) {
    JetscheduleCard(modifier = modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxWidth()
        .height(56.dp),
        elevation = 24.dp) {
        Row(modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                upPress?.let {
                    Box(modifier = modifier.clickable(onClick = upPress)) {
                        Icon(modifier = modifier
                            .padding(3.dp),
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint)
                    }
                }
                    Text(
                        modifier = modifier,
                        text = stringResource(title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
            }
            optionPress?.let {
                Box(modifier = modifier.clickable(onClick = it)) {
                    Icon(modifier = modifier
                        .padding(3.dp),
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = null)
                }
            }
        }
    }
}


@Stable
@Composable
fun JetscheduleTopBar(
    modifier: Modifier,
    title: String,
    upPress: (() -> Unit)? = null,
    optionPress: (() -> Unit)? = null,
) {
    JetscheduleCard(modifier = modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxWidth()
        .height(56.dp),
        elevation = 24.dp) {
        Row(modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                upPress?.let {
                    Box(modifier = modifier.clickable(onClick = upPress)) {
                        Icon(modifier = modifier
                            .padding(3.dp),
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint)
                    }
                }
                Text(
                    modifier = modifier,
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            optionPress?.let {
                Box(modifier = modifier.clickable(onClick = it)) {
                    Icon(modifier = modifier
                        .padding(3.dp),
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = null)
                }
            }
        }
    }
}
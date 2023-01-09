package ru.rescqd.jetschedule.ui.screen.settings.appearance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.JetscheduleSettingMenu
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun SettingsAppearanceScreen(
    modifier: Modifier,
    upPress: () -> Unit,
    viewModel: ThemeViewModel
) {
    JetscheduleScaffold(
        modifier = modifier,
        topBar = {
            JetscheduleTopBar(
                modifier = modifier,
                title = R.string.top_bar_appearance,
                upPress = upPress
            )
        },
        content = {
            Box(modifier = modifier.padding(it)) {
                AppearanceContent(
                    modifier,
                    viewModel
                )
            }
        }
    )
}

@ExperimentalSerializationApi
@Composable
private fun AppearanceContent(
    modifier: Modifier = Modifier,
    viewModel: ThemeViewModel
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp),
    ) {
        item {
            val appTheme by
            viewModel.getAppThemeFlow().collectAsState(initial = viewModel.getAppThemeValue())
            JetscheduleSettingMenu(
                modifier = modifier,
                title = stringResource(id = R.string.appearance_screen_app_theme),
                items = AppTheme.values().toList(),
                onItemClick = viewModel::setAppTheme,
                text = { Text(stringResource(it.displayName)) },
                selectedItem = appTheme
            )
        }
        item {
            val typography by viewModel.getThemeTypographyFlow()
                .collectAsState(initial = viewModel.getThemeTypographyValue())
            JetscheduleSettingMenu(
                modifier = modifier,
                title = stringResource(id = R.string.appearance_screen_typography),
                items = JetscheduleFontFamily.values().toList(),
                onItemClick = viewModel::setTypography,
                text = { Text(stringResource(it.displayName)) },
                selectedItem = typography
            )
        }
        item {
            val colorTheme by
            viewModel.getColorSchemeFlow().collectAsState(
                initial = viewModel.getColorSchemeValue()
            )
            JetscheduleSettingMenu(
                modifier = modifier,
                title = stringResource(id = R.string.appearance_screen_color_scheme),
                items = JetscheduleColorScheme.values().toList(),
                onItemClick = viewModel::setColorScheme,
                text = { Text(text = stringResource(it.displayName)) },
                selectedItem = colorTheme
            )
        }
        item {
            val audienceColorScheme by
            viewModel.getThemeAudienceColorSchemeFlow()
                .collectAsState(
                    initial = viewModel.getThemeAudienceColorSchemeValue()
                )
            JetscheduleSettingMenu(
                modifier = modifier,
                title = stringResource(id = R.string.appearance_screen_audience_color_scheme),
                items = JetscheduleAudienceColorScheme.values().toList(),
                onItemClick = viewModel::setAudienceColorScheme,
                text = { Text(text = stringResource(it.displayName)) },
                selectedItem = audienceColorScheme
            )
        }

    }
}

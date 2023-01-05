package ru.rescqd.jetschedule.ui.screen.settings.appearance

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Colorize
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material.icons.outlined.TextFormat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.JetscheduleTopBar
import ru.rescqd.jetschedule.ui.screen.settings.appearance.view.CustomDropdownMenu
import ru.rescqd.jetschedule.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun SettingsAppearanceScreen (
    modifier: Modifier,
    upPress: () -> Unit,
    viewModel: ThemeViewModel
){
    JetscheduleScaffold(
        modifier = modifier,
        topBar = { JetscheduleTopBar(modifier = modifier, title = R.string.top_bar_appearance, upPress = upPress)},
        content = {Box(modifier = modifier.padding(it)){AppearanceContent(modifier, viewModel)}}
    )
}

@ExperimentalSerializationApi
@Composable
private fun AppearanceContent(
    modifier: Modifier = Modifier,
    viewModel: ThemeViewModel
){
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp),
    ) {
        item {
            val appTheme = viewModel.getAppThemeFlow().collectAsState(initial = viewModel.getAppThemeValue())
            CustomDropdownMenu(
                modifier = modifier,
                icon = when(appTheme.value){
                    AppTheme.SYSTEM -> if (isSystemInDarkTheme()) Icons.Outlined.ModeNight else Icons.Outlined.LightMode
                    AppTheme.LIGHT -> Icons.Outlined.LightMode
                    AppTheme.NIGHT -> Icons.Outlined.ModeNight
                },
                title = R.string.appearance_screen_app_theme,
                items = AppTheme.values().toList(),
                onItemClick = viewModel::setAppTheme,
                text = { Text(stringResource(appTheme.value.displayName)) }
            ) { Text(text = stringResource(it.displayName)) }
        }
        item {
            val typography = viewModel.getThemeTypographyFlow()
                .collectAsState(initial = viewModel.getThemeTypographyValue())
            CustomDropdownMenu(
                modifier = modifier,
                icon = Icons.Outlined.TextFormat,
                title = R.string.appearance_screen_typography,
                items = JetscheduleFontFamily.values().toList(),
                onItemClick = viewModel::setTypography,
                text = { Text(stringResource(typography.value.displayName)) }
            ) { Text(text = stringResource(it.displayName)) }
        }
        item {
            val colorTheme =
                viewModel.getColorSchemeFlow().collectAsState(
                    initial = viewModel.getColorSchemeValue()
                )
            CustomDropdownMenu(
                modifier = modifier,
                icon = Icons.Outlined.Colorize,
                title = R.string.appearance_screen_color_scheme,
                items = JetscheduleColorScheme.values().toList(),
                onItemClick = viewModel::setColorScheme,
                text = { Text(text = stringResource(colorTheme.value.displayName)) }
            ) { Text(text = stringResource(it.displayName)) }
        }
        item {
            val audienceColorScheme =
                viewModel.getThemeAudienceColorSchemeFlow()
                    .collectAsState(
                        initial = viewModel.getThemeAudienceColorSchemeValue()
                    )
            CustomDropdownMenu(
                modifier = modifier,
                icon = Icons.Outlined.Colorize,
                title = R.string.appearance_screen_audience_color_scheme,
                items = JetscheduleAudienceColorScheme.values().toList(),
                onItemClick = viewModel::setAudienceColorScheme,
                text = { Text(text = stringResource(audienceColorScheme.value.displayName)) }
            ) { Text(text = stringResource(it.displayName)) }
        }

    }
}

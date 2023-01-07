package ru.rescqd.jetschedule.ui.home

import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.core.os.ConfigurationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.rescqd.jetschedule.R
import ru.rescqd.jetschedule.ui.theme.AppTheme
import ru.rescqd.jetschedule.ui.SettingsDestinations
import ru.rescqd.jetschedule.ui.components.JetscheduleSurface
import ru.rescqd.jetschedule.ui.screen.schedule.manager.ScheduleManagerScreen
import ru.rescqd.jetschedule.ui.screen.schedule.manager.ScheduleManagerViewModel
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.MainScheduleScreen
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.MainScheduleViewModel
import ru.rescqd.jetschedule.ui.screen.settings.about.AboutApplicationScreen
import ru.rescqd.jetschedule.ui.screen.settings.appearance.SettingsAppearanceScreen
import ru.rescqd.jetschedule.ui.screen.settings.behavior.SettingsBehaviorScreen
import ru.rescqd.jetschedule.ui.screen.settings.behavior.SettingsBehaviorViewModel
import ru.rescqd.jetschedule.ui.screen.settings.destinations.SettingsDestinationScreen
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.RenameSubjectScreen
import ru.rescqd.jetschedule.ui.screen.settings.rename.subject.RenameSubjectViewModel
import ru.rescqd.jetschedule.ui.theme.JetscheduleTheme
import ru.rescqd.jetschedule.ui.theme.ThemeViewModel
import java.util.*


enum class HomeSections(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String,
) {
    SCHEDULE(R.string.home_selection_schedule, Icons.Outlined.Search, "home/schedule"),
    MANAGER(R.string.home_selection_manager, Icons.Outlined.Menu, "home/manager"),
    SETTINGS(R.string.home_selection_settings, Icons.Outlined.Settings, "home/settings"),
}


fun NavGraphBuilder.addHomeGraph(
    modifier: Modifier = Modifier,
    navController: NavController,
    upPress: () -> Unit,
    onManualScreenOpen: (NavBackStackEntry) -> Unit,
) {
    composable(HomeSections.SCHEDULE.route) {
        //val vm = hiltViewModel<CustomSelectViewModel>()
        //CustomSelectScreen(modifier = modifier, viewModel = vm)
        //val vm = hiltViewModel<NewScheduleViewModel>()
        //NewScheduleScreen(modifier = modifier, viewModel = vm)
        val vm = hiltViewModel<MainScheduleViewModel>()
        MainScheduleScreen(modifier = modifier, viewModel = vm)
    }
    composable(HomeSections.MANAGER.route) {
        val vm = hiltViewModel<ScheduleManagerViewModel>()
        ScheduleManagerScreen(modifier = modifier, viewModel = vm)
    }
    composable(HomeSections.SETTINGS.route) {
        SettingsDestinationScreen(
            modifier = modifier,
            navController = navController
        ) { onManualScreenOpen.invoke(it) }
    }

    addSettingsGraph(modifier, upPress)
}

fun NavGraphBuilder.addSettingsGraph(modifier: Modifier, upPress: () -> Unit) {
    composable(
        route = SettingsDestinations.APPLICATION_APPEARANCE.route
    ) {
        val vm = hiltViewModel<ThemeViewModel>()
        SettingsAppearanceScreen(modifier = modifier, upPress = upPress, vm)
    }
    composable(
        route = SettingsDestinations.ABOUT.route
    ) {
        AboutApplicationScreen(modifier, upPress)
    }
    composable(
        route = SettingsDestinations.BEHAVIOR.route
    ) {
        val vm = hiltViewModel<SettingsBehaviorViewModel>()
        SettingsBehaviorScreen(modifier = modifier, viewModel = vm, upPress = upPress)
    }
    composable(
        route = SettingsDestinations.RENAME_SUBJECT.route
    ) {
        val vm = hiltViewModel<RenameSubjectViewModel>()
        RenameSubjectScreen(modifier = modifier, viewModel = vm, upPress = upPress)
    }
}

@Stable
@Composable
fun JetscheduleBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    color: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(color),
) {
    val routes = remember { tabs.map { it.route } }
    val currentSection = tabs.first { it.route == currentRoute }
    JetscheduleSurface(
        color = color,
        contentColor = contentColor,
        shape = RectangleShape
    ) {
        val springSpec = SpringSpec<Float>(
            // Determined experimentally
            stiffness = 800f,
            dampingRatio = 0.8f
        )
        JetscheduleBottomNavLayout(
            selectedIndex = currentSection.ordinal,
            itemCount = routes.size,
            indicator = { JetscheduleBottomNavIndicator() },
            animSpec = springSpec,
            modifier = Modifier.navigationBarsPadding()
        ) {
            val configuration = LocalConfiguration.current

            val currentLocale: Locale =
                ConfigurationCompat.getLocales(configuration).get(0)
                    ?: Locale.getDefault()
            tabs.forEach { section ->
                val selected = section == currentSection
                val tint by animateColorAsState(
                    if (selected) {
                        MaterialTheme.colorScheme.surfaceTint
                    } else {
                        MaterialTheme.colorScheme.surfaceTint.copy(0.5f)
                    }
                )

                val text = stringResource(section.title).uppercase(currentLocale)

                JetscheduleBottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = section.icon,
                            tint = tint,
                            contentDescription = text
                        )
                    },
                    text = {
                        Text(
                            text = text,
                            color = tint,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                    },
                    selected = selected,
                    onSelected = { navigateToRoute(section.route) },
                    animSpec = springSpec,
                    modifier = BottomNavigationItemPadding
                        .clip(BottomNavIndicatorShape)
                )
            }
        }
    }
}

@Stable
@Composable
private fun JetscheduleBottomNavLayout(
    selectedIndex: Int,
    itemCount: Int,
    animSpec: AnimationSpec<Float>,
    indicator: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    // Track how "selected" each item is [0, 1]
    val selectionFractions = remember(itemCount) {
        List(itemCount) { i ->
            androidx.compose.animation.core.Animatable(if (i == selectedIndex) 1f else 0f)
        }
    }
    selectionFractions.forEachIndexed { index, selectionFraction ->
        val target = if (index == selectedIndex) 1f else 0f
        LaunchedEffect(target, animSpec) {
            selectionFraction.animateTo(target, animSpec)
        }
    }

    // Animate the position of the indicator
    val indicatorIndex = remember { androidx.compose.animation.core.Animatable(0f) }
    val targetIndicatorIndex = selectedIndex.toFloat()
    LaunchedEffect(targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex, animSpec)
    }

    Layout(
        modifier = modifier.height(BottomNavHeight),
        content = {
            content()
            Box(Modifier.layoutId("indicator"), content = indicator)
        }
    ) { measurables, constraints ->
        check(itemCount == (measurables.size - 1)) // account for indicator

        // Divide the width into n+1 slots and give the selected item 2 slots
        val unselectedWidth = constraints.maxWidth / (itemCount + 1)
        val selectedWidth = 2 * unselectedWidth
        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }

        val itemPlaceables = measurables
            .filterNot { it == indicatorMeasurable }
            .mapIndexed { index, measurable ->
                // Animate item's width based upon the selection amount
                val width = lerp(unselectedWidth, selectedWidth, selectionFractions[index].value)
                measurable.measure(
                    constraints.copy(
                        minWidth = width,
                        maxWidth = width
                    )
                )
            }
        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(
                minWidth = selectedWidth,
                maxWidth = selectedWidth
            )
        )

        layout(
            width = constraints.maxWidth,
            height = itemPlaceables.maxByOrNull { it.height }?.height ?: 0
        ) {
            val indicatorLeft = indicatorIndex.value * unselectedWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)
            var x = 0
            itemPlaceables.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

@Stable
@Composable
fun JetscheduleBottomNavigationItem(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    selected: Boolean,
    onSelected: () -> Unit,
    animSpec: AnimationSpec<Float>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.selectable(selected = selected, onClick = onSelected),
        contentAlignment = Alignment.Center
    ) {
        // Animate the icon/text positions within the item based on selection
        val animationProgress by animateFloatAsState(if (selected) 1f else 0f, animSpec)
        JetscheduleBottomNavItemLayout(
            icon = icon,
            text = text,
            animationProgress = animationProgress
        )
    }
}

@Stable
@Composable
private fun JetscheduleBottomNavItemLayout(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float,
) {
    Layout(
        content = {
            Box(
                modifier = Modifier
                    .layoutId("icon")
                    .padding(horizontal = TextIconSpacing),
                content = icon
            )
            val scale = lerp(0.6f, 1f, animationProgress)
            Box(
                modifier = Modifier
                    .layoutId("text")
                    .padding(horizontal = TextIconSpacing)
                    .graphicsLayer {
                        alpha = animationProgress
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = BottomNavLabelTransformOrigin
                    },
                content = text
            )
        }
    ) { measurables, constraints ->
        val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraints)
        val textPlaceable = measurables.first { it.layoutId == "text" }.measure(constraints)

        placeTextAndIcon(
            textPlaceable,
            iconPlaceable,
            constraints.maxWidth,
            constraints.maxHeight,
            animationProgress
        )
    }
}

private fun MeasureScope.placeTextAndIcon(
    textPlaceable: Placeable,
    iconPlaceable: Placeable,
    width: Int,
    height: Int,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float,
): MeasureResult {
    val iconY = (height - iconPlaceable.height) / 2
    val textY = (height - textPlaceable.height) / 2

    val textWidth = textPlaceable.width * animationProgress
    val iconX = (width - textWidth - iconPlaceable.width) / 2
    val textX = iconX + iconPlaceable.width

    return layout(width, height) {
        iconPlaceable.placeRelative(iconX.toInt(), iconY)
        if (animationProgress != 0f) {
            textPlaceable.placeRelative(textX.toInt(), textY)
        }
    }
}

@Stable
@Composable
private fun JetscheduleBottomNavIndicator(
    strokeWidth: Dp = 2.dp,
    color: Color = MaterialTheme.colorScheme.surfaceTint,
    shape: Shape = BottomNavIndicatorShape,
) {
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .then(BottomNavigationItemPadding)
            .border(strokeWidth, color, shape)
    )
}

private val TextIconSpacing = 2.dp
private val BottomNavHeight = 56.dp
private val BottomNavLabelTransformOrigin = TransformOrigin(0f, 0.5f)
private val BottomNavIndicatorShape = RoundedCornerShape(percent = 50)
private val BottomNavigationItemPadding = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)

@Preview
@Composable
private fun JetBottomNavPreview() {
    JetscheduleTheme(
        appTheme = AppTheme.NIGHT
    ) {
        JetscheduleBottomBar(
            tabs = HomeSections.values(),
            currentRoute = "home/schedule",
            navigateToRoute = { }
        )
    }
}

package ru.rescqd.jetschedule.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.rescqd.jetschedule.ui.components.JetscheduleScaffold
import ru.rescqd.jetschedule.ui.components.JetscheduleSnackbar
import ru.rescqd.jetschedule.ui.home.JetscheduleBottomBar
import ru.rescqd.jetschedule.ui.home.addHomeGraph
import ru.rescqd.jetschedule.ui.screen.manual.ManualScreen
import ru.rescqd.jetschedule.ui.screen.manual.ManualViewModel
import ru.rescqd.jetschedule.ui.theme.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetscheduleApp(themeViewModel: ThemeViewModel) {
    val appState = rememberJetscheduleAppState()
    val isFirstLaunch = themeViewModel.isFirstLaunchFlow().collectAsState(initial = themeViewModel.isFirstLaunchValue())
    val startDestination = themeViewModel.getStartDestinationValue()
    JetscheduleScaffold(
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                JetscheduleBottomBar(
                    tabs = appState.homeBottomBarTabs,
                    currentRoute = appState.currentRoute!!,
                    navigateToRoute = appState::navigateToBottomBarRoute
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
                modifier = Modifier.systemBarsPadding(),
                snackbar = { snackbarData -> JetscheduleSnackbar(snackbarData) }
            )
        },

    ) { innerPaddingModifier ->
        NavHost(
            navController = appState.navController,
            startDestination = if (!isFirstLaunch.value) MainDestinations.HOME_ROUTE else MainDestinations.MANUAL_ROUTE,
            modifier = Modifier.padding(innerPaddingModifier)
        ) {
            jetscheduleNavGraph(
                upPress = appState::upPress,
                navController = appState.navController,
                onManualScreenOpen = appState::navigateToManualScreen,
                startDestination = startDestination.route
            )
        }
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val MANUAL_ROUTE = "manual"
}

enum class SettingsDestinations(val route: String) {
    APPLICATION_APPEARANCE ( "app_appearance"),
    ABOUT("about"),
    BEHAVIOR("behavior"),
    RENAME_SUBJECT("rename_subject")
}

private fun NavGraphBuilder.jetscheduleNavGraph(
    upPress: () -> Unit,
    navController: NavController,
    onManualScreenOpen: (NavBackStackEntry) -> Unit,
    startDestination: String
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = startDestination
    ) {
        addHomeGraph(navController = navController, upPress = upPress, onManualScreenOpen=onManualScreenOpen)
    }
    composable(route = MainDestinations.MANUAL_ROUTE){
        val vm = hiltViewModel<ManualViewModel>()
        ManualScreen(upPress = upPress, viewModel = vm)
    }
}

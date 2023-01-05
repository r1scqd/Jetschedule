package ru.rescqd.jetschedule.ui

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.rescqd.jetschedule.ui.components.SnackbarManager
import ru.rescqd.jetschedule.ui.home.HomeSections

@Composable
fun rememberJetscheduleAppState(
    navController: NavHostController = rememberNavController(),
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarManager: SnackbarManager = SnackbarManager,
    snackbarHostState: SnackbarHostState = remember{ SnackbarHostState() }
) =
    remember(navController, resources, coroutineScope, snackbarManager) {
        JetscheduleAppState(
            navController,
            resources,
            coroutineScope,
            snackbarManager,
            snackbarHostState
        )
    }

@Stable
class JetscheduleAppState(
    val navController: NavHostController,
    private val resources: Resources,
    coroutineScope: CoroutineScope,
    private val snackbarManager: SnackbarManager,
    val snackbarHostState: SnackbarHostState
) {

    init {
        coroutineScope.launch {
            snackbarManager.messages.collect{ currentMessages ->
                if (currentMessages.isNotEmpty()){
                    val message = currentMessages[0]
                    val text = if (message.messageId == null) message.messageText!! else resources.getText(message.messageId)
                    //TODO("fix message with action")
                    if (message.actionId != null && false){
                        val action = resources.getText(message.actionId)
                        when (snackbarHostState.showSnackbar(text.toString(), action.toString())){
                            SnackbarResult.Dismissed -> message.performAction.invoke()
                            SnackbarResult.ActionPerformed -> message.dismissAction.invoke()
                        }
                    } else {
                        snackbarHostState.showSnackbar(text.toString())
                    }
                    snackbarManager.setMessageShown(message.id)
                }
            }
        }
    }

    // ----------------------------------------------------------
    // BottomBar state source of truth
    // ----------------------------------------------------------

    val homeBottomBarTabs = HomeSections.values()
    private val homeBottomBarRoutes = homeBottomBarTabs.map { it.route }

    // Reading this attribute will cause recompositions when the bottom bar needs shown, or not.
    // Not all routes need to show the bottom bar.
    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in homeBottomBarRoutes

    // ----------------------------------------------------------
    // Navigation state source of truth
    // ----------------------------------------------------------

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToManualScreen(from: NavBackStackEntry){
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.MANUAL_ROUTE)
        }
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
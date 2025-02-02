package com.example.calorieapp


import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.common.snackbar.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


/**
 * A stable class that manages the state of the CalorieApp, including navigation and snackbars.
 *
 * @property snackbarHostState The state for managing snackbars in the UI.
 * @property navController The navigation controller to handle app navigation.
 * @property snackbarManager The manager responsible for displaying snackbars.
 * @property resources The app's resources, used for localizing snackbar messages.
 * @constructor Initializes the app state and sets up a coroutine to listen for snackbar messages.
 *
 * @param coroutineScope The coroutine scope used for launching background tasks.
 */
@Stable
class CalorieAppState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                snackbarHostState.showSnackbar(text)
                snackbarManager.clearSnackbarState()
            }
        }
    }

    /**
     * Navigates back to the previous screen in the navigation stack.
     */
    fun popUp() {
        navController.popBackStack()
    }

    /**
     * Navigates to the specified route while ensuring the same destination isn't launched multiple times.
     *
     * @param route The navigation route to go to.
     */
    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    /**
     * Navigates to the specified route while removing a previous route from the stack.
     *
     * @param route The destination route.
     * @param popUp The route to pop up to without including it.
     */
    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = false }
        }
    }

    /**
     * Clears the navigation stack and navigates to the specified route.
     *
     * @param route The destination route to navigate to.
     */
    fun clearAndNavigate(route: String) {
        try {

            navController.navigate(route) {
                println("NavDebug: Navigating to $route after popBack")
                launchSingleTop = true
                popUpTo(0) { inclusive = true }
            }
            println("NavDebug: Navigation completed successfully")
        } catch (e: Exception) {
            println("NavDebug: Navigation failed with error: ${e.message}")
            e.printStackTrace()
        }
    }
}

package com.example.calorieapp

import android.content.res.Resources
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.model.MealName
import com.example.calorieapp.screens.activity_stats.ActivityScreen
import com.example.calorieapp.screens.adddata.*

import com.example.calorieapp.screens.homescreen.HomeScreen
import com.example.calorieapp.screens.information.MoreAboutYouScreen
import com.example.calorieapp.screens.login.LoginScreen
import com.example.calorieapp.screens.recipe.RecipeDetailsScreen
import com.example.calorieapp.screens.settings.SettingsScreen
import com.example.calorieapp.screens.settings.userchange.UserChangeScreen
import com.example.calorieapp.screens.sign_up.SignUpScreen
import com.example.calorieapp.screens.splash.SplashScreen
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.example.calorieapp.screens.recipe.RecipesScreen
import com.example.calorieapp.screens.settings.goalchange.GoalChangeScreen
import com.example.calorieapp.screens.statistics.StatsScreen
import kotlinx.coroutines.CoroutineScope


/**
 * The main composable function that sets up the CalorieApp UI with navigation and theming.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CalorieApp() {
    CalorieAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()
            val snackbarHostState = appState.snackbarHostState

            // Determine if the TabRow should be displayed based on the current route
            val currentRoute = appState.navController.currentBackStackEntryAsState().value?.destination?.route
            val showTabRow = !(currentRoute == SPLASH_SCREEN ||
                    currentRoute == LOGIN_SCREEN ||
                    currentRoute == SIGN_UP_SCREEN || currentRoute == MORE_ABOUT_YOU_SCREEN)

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.padding(9.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                        }
                    )
                },
                bottomBar = {
                    if (showTabRow) {
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .navigationBarsPadding()

                        ) {
                            if (currentRoute != MORE_ABOUT_YOU_SCREEN) {
                                CalorieAppTabRow(
                                    currentRoute = currentRoute ?: HOME_SCREEN,
                                    openAndPopUp = { route, popUp ->
                                        appState.navigateAndPopUp(
                                            route,
                                            popUp
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            ) { paddingValues ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    calorieGraph(appState)
                }
            }
        }
    }
}

/**
 * Creates and remembers the application state, including navigation and snackbar handling.
 */
@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        CalorieAppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
    }


/**
 * Provides access to the application's resources.
 */
@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

/**
 * Defines the navigation graph for the application.
 *
 * @param appState The application state that manages navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.calorieGraph(
    appState: CalorieAppState,
    ) {

    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(HOME_SCREEN) {
        HomeScreen(openScreen = { route -> appState.navigate(route) })
    }


    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(MORE_ABOUT_YOU_SCREEN) {
        MoreAboutYouScreen(openScreen = { route -> appState.navigate(route) })
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

    composable(USER_CHANGE_SCREEN) {
        UserChangeScreen(
            popUpScreen = { appState.popUp() }
        )
    }

    composable(GOAL_CHANGE_SCREEN) {
        GoalChangeScreen(
            popUpScreen = { appState.popUp() }
        )
    }


    composable(ACTIVITY_SCREEN) {
        ActivityScreen(
            popUpScreen = { appState.popUp() }
        )
    }

    composable(MEAL_TIME_SCREEN) {
        MealTimeScreen(
            openScreen = { route -> appState.navigate(route) },
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
        )
    }

    composable("$ADD_DATA_SCREEN/{mealType}/{date}") { backStackEntry ->
        val mealName = backStackEntry.arguments?.getString("mealType") ?: MealName.Breakfast.name
        val date = backStackEntry.arguments?.getString("date") ?: formatDateToString(System.currentTimeMillis())

        AddDataScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            mealName = mealName,
            date = date
        )
    }

    composable(RECIPES_SCREEN) {
        RecipesScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }

    composable("$RECIPE_DETAILS_SCREEN/{recipeName}") { backStackEntry ->
        val recipeName = backStackEntry.arguments?.getString("recipeName") ?: "DefaultRecipe"
        RecipeDetailsScreen(
            recipeName = recipeName,
            popUpScreen = { appState.popUp() }
        )
    }

    composable(STATS_SCREEN) {
        StatsScreen(
            openScreen = { route -> appState.navigate(route) }
        )
    }
}


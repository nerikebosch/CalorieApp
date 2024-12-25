package com.example.calorieapp

import android.content.res.Resources
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.screens.homescreen.HomeScreen
import com.example.calorieapp.screens.login.LoginScreen
import com.example.calorieapp.screens.settings.SettingsScreen
import com.example.calorieapp.screens.sign_up.SignUpScreen
import com.example.calorieapp.screens.splash.SplashScreen
import com.example.calorieapp.ui.theme.CalorieAppTheme
import kotlinx.coroutines.CoroutineScope

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CalorieApp() {
    CalorieAppTheme{
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()
            val snackbarHostState = appState.snackbarHostState
                //
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.padding(9.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                        }
                    )
                }
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = LOGIN_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    calorieGraph(appState)
                }
            }
        }
    }
}


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


@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.calorieGraph(appState: CalorieAppState) {

    composable(HOME_SCREEN) {
        HomeScreen(openScreen = { route -> appState.navigate(route) })
    }


    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            //openScreen = { route -> appState.navigate(route) }
        )
    }

    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }
}
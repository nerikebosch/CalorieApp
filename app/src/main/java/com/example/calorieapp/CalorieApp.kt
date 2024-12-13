//package com.example.calorieapp
//
//import android.content.res.Resources
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.ReadOnlyComposable
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalContext
//import androidx.navigation.compose.NavHost
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.calorieapp.loginscreens.LoginScreen
//import com.example.calorieapp.loginscreens.SignUpScreen
//import com.example.calorieapp.ui.theme.CalorieAppTheme
//import com.example.calorieapp.common.snackbar.SnackbarManager
//import com.example.calorieapp.screens.homescreen.HomeScreen
//import kotlinx.coroutines.CoroutineScope
//
//@Composable
//@OptIn(ExperimentalMaterial3Api::class)
//fun CalorieApp() {
//    CalorieAppTheme{
//        Surface(color = MaterialTheme.colorScheme.background) {
//            val appState = rememberAppState()
//
//            Scaffold( /*TODO snackbar*/ ) { innerPaddingModifier ->
//                NavHost(
//                    navController = appState.navController,
//                    startDestination = "login",
//                    modifier = Modifier.padding(innerPaddingModifier)
//                ) {
//            }
//        }
//    }
//}
//
//
//@Composable
//fun rememberAppState(
//    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
//    navController: NavHostController = rememberNavController(),
//    snackbarManager: SnackbarManager = SnackbarManager,
//    resources: Resources, // = resources(),
//    coroutineScope: CoroutineScope = rememberCoroutineScope()
//) =
//    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
//        CalorieAppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
//    }
//
//
//@Composable
//@ReadOnlyComposable
//fun resources(): Resources {
//    LocalConfiguration.current
//    return LocalContext.current.resources
//}
//
//fun NavGraphBuilder.calorieGraph(appState: CalorieAppState) {
//    composable("home") {
//        HomeScreen()
//    }
//
//
//    composable("login") {
//        LoginScreen(navController = rememberNavController(),
//            ) { }
//    }
//
//    composable("signup") {
//        SignUpScreen(navController = rememberNavController())
//    }
//
//}
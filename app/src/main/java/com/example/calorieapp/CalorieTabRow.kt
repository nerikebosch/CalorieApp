package com.example.calorieapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


/**
 * Composable function that renders a tab row navigation for the CalorieApp.
 *
 * @param currentRoute The current route of the application to determine the selected tab.
 * @param openAndPopUp A lambda function that handles navigation when a tab is selected,
 * taking the new route and the previous route as parameters.
 */
@Composable
fun CalorieAppTabRow(
    currentRoute: String,
    openAndPopUp: (String, String) -> Unit
) {
    // List of routes corresponding to the tabs
    val tabRoutes = listOf(HOME_SCREEN, RECIPES_SCREEN, MEAL_TIME_SCREEN, STATS_SCREEN)
    val titles = listOf("Home", "Recipes", "Add data", "Statistics")

    // Keeps track of the last valid tab index to prevent navigation errors
    var lastValidTabIndex by remember { mutableStateOf(0) }

    /**
     * Determines the selected tab index based on the current route.
     * If the route is invalid, it falls back to the last valid index.
     */
    val selectedIndex = when (currentRoute) {
        in tabRoutes -> {
            tabRoutes.indexOf(currentRoute)
                .also { lastValidTabIndex = it }
        }
        else -> lastValidTabIndex
    }

    // UI container for the tab row
    Box(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = selectedIndex) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = (index == selectedIndex),
                    onClick = {
                        println("TabRowDebug: Clicked tab $index")
                        // Map the tab index back to route
                        val route = tabRoutes[index]
                        println("TabRowDebug: Navigating to $route from $currentRoute")
                        if (route != currentRoute) {
                            openAndPopUp(route, tabRoutes[lastValidTabIndex])
                        }
                    }
                )
            }
        }
    }
}
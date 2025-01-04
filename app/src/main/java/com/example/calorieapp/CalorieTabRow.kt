package com.example.calorieapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CalorieAppTabRow(
    currentRoute: String,
    onTabSelected: (String) -> Unit
) {
    val titles = listOf("Home", "Recipes", "Add data", "Statistics")

    // Map the current route to tab index
    val selectedIndex = when (currentRoute) {
        HOME_SCREEN -> 0
        HOME_SCREEN -> 1
        MEAL_TIME_SCREEN -> 2
        MEAL_TIME_SCREEN -> 3
        else -> 0
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = selectedIndex) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = index == selectedIndex,
                    onClick = {
                        println("TabRowDebug: Clicked tab $index")
                        // Map the tab index back to route
                        val route = when (index) {
                            0 -> HOME_SCREEN
                            1 -> HOME_SCREEN
                            2 -> MEAL_TIME_SCREEN
                            3 -> MEAL_TIME_SCREEN
                            else -> HOME_SCREEN
                        }
                        onTabSelected(route)

                    }
                )
            }
        }
    }
}
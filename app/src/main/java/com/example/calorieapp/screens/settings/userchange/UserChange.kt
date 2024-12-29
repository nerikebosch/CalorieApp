package com.example.calorieapp.screens.settings.userchange

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun UserChangeScreen(
    openAndPopUp: (String, String) -> Unit,
//    viewModel: UserChangeViewModel = hiltViewModel()
) {
    Box() {
        Text(text = "User Change Screen")
        Text(text = "{user.email}")
    }
}
package com.example.calorieapp.screens.settings.userchange

import android.R.attr.text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calorieapp.common.composable.LabelTextField
import com.example.calorieapp.common.composable.TextActionToolbar
import com.example.calorieapp.common.ext.spacer
import com.example.calorieapp.common.ext.textCard
import com.example.calorieapp.common.ext.toolbarActions
import com.example.calorieapp.model.User
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.example.calorieapp.R.string as AppText

/**
 * Composable function for the User Change screen. Allows the user to edit their profile information
 * such as name and surname.
 *
 * @param popUpScreen A lambda function to handle the back navigation or screen closure.
 * @param viewModel The ViewModel for managing the state and business logic of the User Change screen.
 */
@Composable
fun UserChangeScreen(
    popUpScreen: () -> Unit,
    viewModel: UserChangeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    println("UserChangeScreenDebug: Screen loaded")
    println("UserChangeScreenDebug: User ID: ${user.id}, Name: ${user.name}, Surname: ${user.surname}")

        UserChangeScreenContent(
            onDoneClick = { viewModel.onDoneClick(popUpScreen) },
            user = user,
            onNameChange = viewModel::onNameChange,
            onSurnameChange = viewModel::onSurnameChange
        )

}

/**
 * Composable function for displaying the content of the User Change screen.
 * Provides fields for editing user details and a save action.
 *
 * @param onDoneClick A callback function invoked when the save button is clicked.
 * @param user The current user object containing name and surname.
 * @param onNameChange A lambda function to handle name changes.
 * @param onSurnameChange A lambda function to handle surname changes.
 */
@Composable
fun UserChangeScreenContent(
    onDoneClick: () -> Unit = {},
    user: User,
    onNameChange: (String) -> Unit = {},
    onSurnameChange: (String) -> Unit = {},

) {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextActionToolbar(
                title = AppText.your_data,
                text = "Save",
                modifier = Modifier.toolbarActions(),
                endAction = onDoneClick
            )
        }
        Spacer(modifier = Modifier.spacer())
        Text(text = "Your name", style = MaterialTheme.typography.labelLarge)
        LabelTextField(
            value = user.name,
            onValueChange = onNameChange ,
            label = "Your name",
            modifier = Modifier.textCard(),
            trailing = { Icon(Icons.Filled.Person, contentDescription = "Menu") }
        )
        Text(text = "Your surname", style = MaterialTheme.typography.labelLarge)
        LabelTextField(
            value = user.surname,
            onValueChange = onSurnameChange,
            label = "Your surname",
            modifier = Modifier.textCard(),
            trailing = { Icon(Icons.Filled.Person, contentDescription = "Menu") }
        )


        Spacer(modifier = Modifier.spacer())

    }
}

/**
 * Composable function for displaying a loading state during user data update or retrieval.
 * Shows a circular progress indicator centered in the screen.
 */
@Composable
fun LoadingChangeScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center)  {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}

/**
 * Preview for the User Change screen with mocked user data.
 * Demonstrates how the screen appears with sample inputs.
 */
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun UserChangeScreenPreview() {
    CalorieAppTheme {
        var previewUser by remember { mutableStateOf(User(name = "John", surname = "Doe")) }

        UserChangeScreenContent(
            onDoneClick = {},
            user = previewUser,
            onNameChange = { previewUser = previewUser.copy(name = it) },
            onSurnameChange = { previewUser = previewUser.copy(surname = it) }

        )
    }
}


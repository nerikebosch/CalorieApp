package com.example.calorieapp.screens.settings.userchange

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calorieapp.common.composable.LabelTextField
import com.example.calorieapp.common.composable.TextActionToolbar
import com.example.calorieapp.common.ext.spacer
import com.example.calorieapp.common.ext.textCard
import com.example.calorieapp.common.ext.toolbarActions
import com.example.calorieapp.model.User
import com.example.calorieapp.screens.settings.SettingsViewModel
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.example.calorieapp.R.string as AppText


@Composable
fun UserChangeScreen(
    popUpScreen: () -> Unit,
    viewModel: UserChangeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState(initial = User())

    println("UserChangeScreenDebug: screen Loaded")
    println("UserChangeScreenDebug: User ID: ${user.id}, Name: ${user.name}, Surname: ${user.surname}")

    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    if (isLoading) {

        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    } else {
        UserChangeScreenContent(
            onDoneClick = { viewModel.onDoneClick(popUpScreen) },
            user = user,
            onNameChange = viewModel::onNameChange,
            onSurnameChange = viewModel::onSurnameChange
        )
    }

}
//@Composable
//fun UserChangeScreen(
//    popUpScreen: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    println("UserChangeScreenDebug: screen Loaded")
//    Text(text = "hello", modifier = modifier)
//}

@Composable
fun UserChangeScreenContent(
    onDoneClick: () -> Unit = {},
    user: User,
    onNameChange: (String) -> Unit = {},
    onSurnameChange: (String) -> Unit = {}
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
        LabelTextField(
            value = "${user.name}", // user.name
            onValueChange = onNameChange , //viewModel::onNameChange,
            label = "Your name",
            modifier = Modifier.textCard()
        )
        LabelTextField(
            value = user.surname,
            onValueChange = onSurnameChange,
            label = "Your surname",
            modifier = Modifier.textCard()
        )
        Spacer(modifier = Modifier.spacer())

    }
}

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


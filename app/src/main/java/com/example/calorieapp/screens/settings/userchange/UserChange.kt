package com.example.calorieapp.screens.settings.userchange

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calorieapp.common.composable.ActionToolbar
import com.example.calorieapp.common.composable.BasicToolbar
import com.example.calorieapp.common.composable.LabelTextField
import com.example.calorieapp.common.composable.TextActionToolbar
import com.example.calorieapp.common.ext.card
import com.example.calorieapp.common.ext.fieldModifier
import com.example.calorieapp.common.ext.spacer
import com.example.calorieapp.common.ext.textCard
import com.example.calorieapp.common.ext.toolbarActions
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.example.calorieapp.R.string as AppText

@Composable
fun UserChangeScreen(
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserChangeViewModel = hiltViewModel()
) {
    //val user by viewModel.user.collectAsState(initial = User())

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }



}

@Composable
fun UserChangeScreenContent(
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
                endAction = { }
            )
        }
        LabelTextField(
            value = "Nerike", // user.name
            onValueChange = {}, //viewModel::onNameChange,
            label = "Your name",
            modifier = Modifier.textCard()
        )
        LabelTextField(
            value = "Bosch",
            onValueChange = {},
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
        UserChangeScreenContent()
    }
}


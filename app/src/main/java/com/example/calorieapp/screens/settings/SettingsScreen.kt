package com.example.calorieapp.screens.settings


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.common.composable.*
import com.example.calorieapp.common.ext.*
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.example.calorieapp.R.string as AppText

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    restartApp: (String) -> Unit,
    //openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(AppText.settings)

        Spacer(modifier = Modifier.spacer())

        SignOutCard { viewModel.onSignOutClick(restartApp) }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(
        AppText.sign_out,
        Icons.Filled.Settings,
        "",
        Modifier.card()) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.sign_out_title)) },
            text = { Text(stringResource(AppText.sign_out_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.sign_out) {
                    signOut()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}


@ExperimentalMaterial3Api
@Composable
fun ChangeDataCard() {

}

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SettingsScreenPreview() {
    CalorieAppTheme {
        // Create a simplified version for preview
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicToolbar(AppText.settings)

            Spacer(modifier = Modifier.spacer())

            SignOutCard { }
        }
    }
}
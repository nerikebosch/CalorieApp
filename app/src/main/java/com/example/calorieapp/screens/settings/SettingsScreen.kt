package com.example.calorieapp.screens.settings


import android.R.attr.fontFamily
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calorieapp.common.composable.*
import com.example.calorieapp.common.ext.*
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.example.calorieapp.ui.theme.Roboto
import com.google.android.play.integrity.internal.f
import com.example.calorieapp.R.string as AppText


/**
 * Composable function for displaying the Settings screen.
 *
 * @param restartApp Function to restart the app with a specific screen.
 * @param openScreen Function to navigate to a specified screen.
 * @param modifier Modifier for layout adjustments.
 * @param viewModel ViewModel for handling settings-related operations.
 */
@ExperimentalMaterial3Api
@Composable 
fun SettingsScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    LaunchedEffect(true) { viewModel.onUserLoad() }
    println("SettingsScreenDebug: Screen loaded, user: $user")

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(AppText.settings)
        Text(
            text = "Your Profile",
            modifier = Modifier.padding(16.dp, 8.dp).align(Alignment.Start),
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = Roboto
        )

        ChangeDataCard(
            label = user.name + " " + user.surname,
            detail = user.email
        ) { viewModel.onUserChangeClick(openScreen) }

        ChangeDataCard(
            label = stringResource(AppText.goal_change),
            detail = "",
        ) { viewModel.onGoalChangeClick(openScreen) }


        ChangeDataCard(
            label = stringResource(AppText.your_activity),
            detail = "See how many steps you walked today",
        ) { viewModel.onOpenActivityClick(openScreen)}

        ChangeDataCard(
            label = "Maps",
            detail = "See your location",
        ) { viewModel.onMapsClick(openScreen)}
        Spacer(modifier = Modifier.spacer())
        SignOutCard { viewModel.onSignOutClick(restartApp) }


    }
}


/**
 * Composable function for displaying a sign-out card with a confirmation dialog.
 *
 * @param signOut Function triggered when the user confirms sign-out.
 */
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


/**
 * Composable function for displaying a card with user data that can be changed.
 *
 * @param label The label describing the data.
 * @param detail The current value of the data.
 * @param onClick Callback function when the card is clicked.
 */
@ExperimentalMaterial3Api
@Composable
fun ChangeDataCard(
    label: String,
    detail: String,
    onClick: () -> Unit) {
    TextCardEditor(
        label = label,
        detail = detail,
        icon = Icons.Filled.ChevronRight,
        onEditClick = { onClick() },
        modifier = Modifier.textCard()
    )
}

/**
 * Preview function for the Settings screen.
 */
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
            Text(
                text = "Your Profile",
                modifier = Modifier.padding(16.dp, 8.dp).align(Alignment.Start),
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = Roboto
            )

            ChangeDataCard(
                label = "John Doe",
                detail = "john.c.breckinridge@altostrat.com",
                onClick = {}
            )
            ChangeDataCard(
                label = stringResource(AppText.goal_change),
                detail = "",
                onClick = {}
            )
            ChangeDataCard(
                label = stringResource(AppText.your_activity),
                detail = "See how many steps you walked today",
                onClick = {}
            )
            ChangeDataCard(
                label = "Maps",
                detail = "See your location",
                onClick = {}
            )
            Spacer(modifier = Modifier.spacer())
            SignOutCard { }


        }
    }
}
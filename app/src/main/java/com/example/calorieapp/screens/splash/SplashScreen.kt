package com.example.calorieapp.screens.splash


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.R
import com.example.calorieapp.common.composable.BasicButton
import com.example.calorieapp.common.ext.basicButton
import com.example.calorieapp.ui.theme.CalorieAppTheme
import kotlinx.coroutines.delay
import com.example.calorieapp.R.string as AppText


private const val SPLASH_TIMEOUT = 1000L

/**
 * Composable function representing the Splash screen. Displays the app's logo,
 * name, and catchphrase while performing a startup delay and checking the user's authentication state.
 *
 * @param openAndPopUp A lambda function to navigate to a new screen and remove the previous screen from the back stack.
 * @param modifier Modifier for styling and layout.
 * @param viewModel The ViewModel for the Splash screen, provided via Hilt dependency injection.
 */
@Composable
fun SplashScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    var hasNavigated by remember { mutableStateOf(false) }

    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.showError.value) {
            Text(text = stringResource(AppText.generic_error))

            BasicButton(AppText.try_again, Modifier.basicButton()) {
                viewModel.onAppStart(
                    openAndPopUp
                )
            }
        } else {
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = AppText.app_icon),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(14.dp))

            Text(text = stringResource(AppText.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = stringResource(AppText.app_catchphrase),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(30.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        }
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        println("SplashDebug: Launching initial navigation")
        viewModel.onAppStart(openAndPopUp)
    }
}

/**
 * Content for the Splash screen, displaying either an error message with a retry button
 * or the app's logo, name, and a loading indicator.
 *
 * @param showError A flag indicating whether an error message should be displayed.
 * @param onTryAgainClick A callback function invoked when the "Try Again" button is clicked.
 * @param modifier Modifier for styling and layout.
 */
@Composable
private fun SplashScreenContent(
    showError: Boolean,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showError) {
            Text(
                text = stringResource(AppText.generic_error),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicButton(
                AppText.try_again,
                Modifier.basicButton(),

            ) {
                onTryAgainClick()
            }
        } else {
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = AppText.app_icon),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(14.dp))

            Text(text = stringResource(AppText.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = stringResource(AppText.app_catchphrase),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(30.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    CalorieAppTheme {
        SplashScreenContent(
            showError = true,
            onTryAgainClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenLoadingPreview() {
    CalorieAppTheme {
        SplashScreenContent(
            showError = false,
            onTryAgainClick = {}
        )
    }
}
package com.example.calorieapp.screens.sign_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.R
import com.example.calorieapp.common.composable.EmailField
import com.example.calorieapp.common.composable.LabelTextField
import com.example.calorieapp.common.composable.PasswordSignUpTextField
import com.example.calorieapp.common.ext.fieldModifier
import com.example.calorieapp.ui.theme.Roboto
import com.example.calorieapp.R.string as AppText


/**
 * Composable function for the Sign-Up screen.
 *
 * @param openAndPopUp Function to navigate between screens.
 * @param viewModel ViewModel responsible for handling Sign-Up logic.
 */
@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    SignUpScreenContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onSurnameChange = viewModel::onSurnameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignUpClick = { viewModel.onSignUpClick(openAndPopUp) },
        onLoginScreenClick = { viewModel.onLoginScreenClick(openAndPopUp) }
    )

}

/**
 * Composable function that represents the content of the Sign-Up screen.
 *
 * @param uiState The current UI state containing user input.
 * @param onNameChange Callback when the name input changes.
 * @param onSurnameChange Callback when the surname input changes.
 * @param onEmailChange Callback when the email input changes.
 * @param onPasswordChange Callback when the password input changes.
 * @param onSignUpClick Callback when the Sign-Up button is clicked.
 * @param onLoginScreenClick Callback when navigating to the Login screen.
 */
@Composable
fun SignUpScreenContent(
    uiState: SignUpUiState,
    onNameChange: (String) -> Unit,
    onSurnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onLoginScreenClick: () -> Unit,
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by remember { mutableStateOf(false) }

    val fieldModifier = Modifier.fieldModifier()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                SignUpTopSection()
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LabelTextField(uiState.name, onNameChange, fieldModifier, label = "Name")
                    Spacer(modifier = Modifier.height(15.dp))

                    LabelTextField(uiState.surname, onSurnameChange, fieldModifier, label = "Surname")
                    Spacer(modifier = Modifier.height(15.dp))

                    EmailField(uiState.email, onEmailChange, fieldModifier)
                    Spacer(modifier = Modifier.height(15.dp))

                    PasswordSignUpTextField(uiState.password, onPasswordChange)
                    Spacer(modifier = Modifier.height(40.dp))

                    // Display error message if exists
                    errorMessage?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }

                    // Sign-up button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        onClick = {
                            when {
                                uiState.name.isBlank() -> errorMessage = "Name is required"
                                uiState.surname.isBlank() -> errorMessage = "Surname is required"
                                uiState.email.isBlank() -> errorMessage = "Email is required"
                                uiState.password.isBlank() -> errorMessage = "Password is required"

                                else -> {
                                    errorMessage = null
                                    onSignUpClick()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(size = 4.dp)
                    ) {
                        Text(
                            fontSize = 14.sp,
                            text = "Sign Up",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Navigate to login if already have an account
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                onLoginScreenClick()
                            },
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal
                                )
                            ) {
                                append("Already have an account? ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 14.sp,
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Medium
                                )
                            ) {
                                append("Login")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}


/**
 * Composable function that represents the top section of the Sign-Up screen, including the logo and app name.
 */
@Composable
fun SignUpTopSection() {
    val uiColor = MaterialTheme.colorScheme.primary

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.35f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )


        Row(
            modifier = Modifier.padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = AppText.app_icon),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(
                    text = stringResource(id = AppText.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = stringResource(id = AppText.app_catchphrase),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter),
            text = stringResource(id = AppText.signup),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}


@Preview
@Composable
fun SignUpScreenPreview() {
    val uiState = SignUpUiState(
        name = "nicky",
        email = "emailtest.com",
    )

    SignUpScreenContent(
        uiState = uiState,
        onNameChange = {},
        onSurnameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onSignUpClick = {},
        onLoginScreenClick = {}
    )

}
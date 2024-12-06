package com.example.calorieapp.loginscreens

//

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.calorieapp.R
import com.example.calorieapp.ui.theme.Roboto

@Composable
fun SignUpScreen(navController: NavController) {
    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection()
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                //SignUpSection(navController, onSignUpClick = { /* Handle Sign Up Logic */ })
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}


@Composable
fun SignUpSection(
    viewModel: SignUpViewModel = remember { SignUpViewModel() },
    navController: NavController,
    //onSignUpClick: () -> Unit
) {
    val nameState = remember { mutableStateOf("") }
    val surnameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val repeatPasswordState = remember { mutableStateOf("") }

    // Error state
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 30.dp)) {
        LoginTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = "Name"
        )
        Spacer(modifier = Modifier.height(15.dp))

        LoginTextField(
            value = surnameState.value,
            onValueChange = { surnameState.value = it },
            label = "Surname"
        )
        Spacer(modifier = Modifier.height(15.dp))

        LoginTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = "Email"
        )
        Spacer(modifier = Modifier.height(15.dp))

        PasswordSignUpTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
        )

        Spacer(modifier = Modifier.height(15.dp))

        ConfirmPasswordSignUpTextField(
            value = repeatPasswordState.value,
            confirmValue = passwordState.value,
            onValueChange = { repeatPasswordState.value = it },
        )

        Spacer(modifier = Modifier.height(20.dp))


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
            onClick = { /* Handle Sign Up Logic */
                when {
                    nameState.value.isBlank() -> errorMessage = "Name is required"
                    surnameState.value.isBlank() -> errorMessage = "Surname is required"
                    emailState.value.isBlank() -> errorMessage = "Email is required"
                    passwordState.value.isBlank() -> errorMessage = "Password is required"
                    passwordState.value != repeatPasswordState.value -> {
                        errorMessage = "Passwords do not match"
                    }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 4.dp)
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Sign Up Text (or navigate to the login screen)
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    // Navigate to the LoginScreen when the "Login" text is clicked
                    navController.navigate("login")
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
    }
}



@Composable
fun TopSection() {
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
            modifier = Modifier.padding(top = 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.app_icon),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = stringResource(id = R.string.app_catchphrase),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter),
            text = stringResource(id = R.string.signup),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}


@Preview
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    SignUpScreen(navController = navController)

}
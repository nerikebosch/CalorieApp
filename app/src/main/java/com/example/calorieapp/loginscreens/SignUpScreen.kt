package com.example.calorieapp.loginscreens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.calorieapp.R
import com.example.calorieapp.ui.theme.Roboto


@Composable
fun SignUpScreen() {
    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection()
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                SignUpSection()
                Spacer(modifier = Modifier.height(30.dp))
                SocialMediaSection()

                val uiColor = MaterialTheme.colorScheme.primary
                Box(modifier = Modifier
                    .fillMaxHeight(fraction = 0.8f)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ){

                }

            }
        }

    }
}

@Composable
private fun SocialMediaSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Or continue with",
            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFFFFFFFF))
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialMediaLogIn(
                icon = R.drawable.google,
                text = "Google",
                modifier = Modifier.weight(1f)
            ) {

            }
            Spacer(modifier = Modifier.width(20.dp))
            SocialMediaLogIn(
                icon = R.drawable.facebook,
                text = "Facebook",
                modifier = Modifier.weight(1f)
            ) {

            }
        }
    }
}

@Composable
private fun SignUpSection() {
    val nameState = remember { mutableStateOf("") }
    val surnameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val repeatPasswordState = remember { mutableStateOf("") }

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

        LoginTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = "Password"
        )
        Spacer(modifier = Modifier.height(15.dp))

        LoginTextField(
            value = repeatPasswordState.value,
            onValueChange = { repeatPasswordState.value = it },
            label = "Repeat Password"
        )
        Spacer(modifier = Modifier.height(20.dp))

                    // Sign-up button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        onClick = { /* Handle Sign Up Logic */ },
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
                        modifier = Modifier.align(Alignment.CenterHorizontally),
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
                        },
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
            }


@Composable
private fun TopSection() {
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
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.icon),
                contentDescription = stringResource(id = R.string.app_icon),
                tint = uiColor
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = uiColor
                )
                Text(
                    text = stringResource(id = R.string.app_catchphrase),
                    style = MaterialTheme.typography.titleMedium,
                    color = uiColor
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
    SignUpScreen()


}
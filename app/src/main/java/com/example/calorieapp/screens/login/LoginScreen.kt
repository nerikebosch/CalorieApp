package com.example.calorieapp.screens.login

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.R
import com.example.calorieapp.common.composable.EmailField
import com.example.calorieapp.common.composable.LoginButton
import com.example.calorieapp.common.composable.PasswordTextField
import com.example.calorieapp.common.ext.fieldModifier
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.example.calorieapp.ui.theme.Roboto
import com.example.calorieapp.ui.theme.primaryDark
import com.example.calorieapp.ui.theme.primaryLight
import com.example.calorieapp.R.string as AppText


@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    LoginScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignInClick = { viewModel.onSignInClick(openAndPopUp) },
        onForgotPasswordClick = viewModel::onForgotPasswordClick,
        onGoogleSignInClick = { viewModel.onGoogleSignInClick(openAndPopUp) },
        onFacebookSignInClick = { viewModel::onFacebookSignInClick },
        onSignUpScreenClick = { viewModel.onSignUpScreenClick(openAndPopUp) }
    )
}

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onFacebookSignInClick: () -> Unit,
    onSignUpScreenClick: () -> Unit
) {
    val uiColor = MaterialTheme.colorScheme.primary

    Surface {
        Column(Modifier.fillMaxSize()) {
            // Show app name and logo
            TopSection()
            Spacer(modifier = Modifier.height(26.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EmailField(uiState.email, onEmailChange, Modifier.fieldModifier())

                    Spacer(modifier = Modifier.height(15.dp))

                    PasswordTextField(uiState.password, onPasswordChange, Modifier.fieldModifier())

                    Spacer(modifier = Modifier.height(15.dp))

                    //Forgot password section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            modifier = Modifier.clickable { onForgotPasswordClick() }, // Forgot password function
                            text = "Forgot password?",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    LoginButton(
                        text = AppText.sign_in,
                        modifier = Modifier.fillMaxSize()
                    ) { onSignInClick() }

//            BasicTextButton(AppText.forgot_password, Modifier.textButton()) {
//                onForgotPasswordClick()
//            }

                }

                // SOCIAL MEDIA LOGIN SECTION
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Or continue with",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SocialMediaLogIn(
                            icon = R.drawable.google,
                            text = "Google",
                            modifier = Modifier.weight(1f),
                            onSignInClick = onGoogleSignInClick
                        )

                        Spacer(modifier = Modifier.width(20.dp))
                        SocialMediaLogIn(
                            icon = R.drawable.facebook,
                            text = "Facebook",
                            modifier = Modifier.weight(1f),
                            onSignInClick = onFacebookSignInClick
                        )

                    }
                } // END OF SOCIAL MEDIA LOGIN SECTION


                // SIGN UP PROMPT SECTION
                Box(modifier = Modifier
                    .fillMaxHeight(fraction = 0.8f)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFF94A3B8),
                                    fontSize = 14.sp,
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal
                                )
                            ) {
                                append("Don't have an account?")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = uiColor,
                                    fontSize = 14.sp,
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Medium
                                )
                            ) {
                                append(" ")
                                append("Create now")
                            }
                        },
                        modifier = Modifier.clickable {
                            // Navigate to the SignUpScreen
                            onSignUpScreenClick()
                        }
                    )
                }
            }
        }
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
                .fillMaxHeight(fraction = 0.5f),
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
            text = stringResource(id = AppText.login),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
    Spacer(modifier = Modifier.height(26.dp))
}


@Composable
fun SocialMediaLogIn(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    onSignInClick: () -> Unit,
) {

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .socialMedia()
            .clickable { onSignInClick() }
            .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
        )

    }

}


@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.socialMedia(): Modifier = composed {
    if (isSystemInDarkTheme()) {
        background(Color.Transparent).border(
            width = 1.dp,
            color = primaryDark,
            shape = RoundedCornerShape(4.dp)
        )
    } else {
        background(primaryLight)
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val uiState = LoginUiState(
        email = "email@test.com"
    )

    CalorieAppTheme {
        LoginScreenContent(
            uiState = uiState,
            onEmailChange = { },
            onPasswordChange = { },
            onSignInClick = { },
            onForgotPasswordClick = { },
            onGoogleSignInClick = { },
            onFacebookSignInClick = { },
            onSignUpScreenClick = { }
        )
    }
}
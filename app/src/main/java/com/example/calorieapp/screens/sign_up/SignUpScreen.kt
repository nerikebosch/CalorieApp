package com.example.calorieapp.screens.sign_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.calorieapp.LOGIN_SCREEN
import com.example.calorieapp.R
import com.example.calorieapp.SIGN_UP_SCREEN
import com.example.calorieapp.common.composable.*
import com.example.calorieapp.common.ext.basicButton
import com.example.calorieapp.common.ext.fieldModifier
import com.example.calorieapp.ui.theme.Roboto
import com.example.calorieapp.R.string as AppText


@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val fieldModifier = Modifier.fieldModifier()

    //BasicToolbar(AppText.create_account)
    SignUpTopSection()

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LabelTextField(uiState.name,  viewModel::onNameChange, fieldModifier,label = "Name")
        LabelTextField(uiState.surname, viewModel::onSurnameChange, fieldModifier,label = "Surname")
        EmailField(uiState.email, viewModel::onEmailChange, fieldModifier)
        PasswordField(uiState.password, viewModel::onPasswordChange, fieldModifier)

        BasicButton(AppText.create_account, Modifier.basicButton()) {
            viewModel.onSignUpClick(openAndPopUp)
        }
    }

    Text(
        modifier = Modifier
            .clickable {
                // Navigate to the LoginScreen when the (all the line) "Login" text is clicked
                openAndPopUp(LOGIN_SCREEN, SIGN_UP_SCREEN)
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
            painter = painterResource(id = com.example.calorieapp.R.drawable.shape),
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

@Composable
@Preview
fun SignUpScreenPreview() {
    SignUpScreen(openAndPopUp = { _, _ -> })

}
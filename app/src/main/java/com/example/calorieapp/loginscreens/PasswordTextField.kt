package com.example.calorieapp.loginscreens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.regex.Pattern
/**
 * Jetpack Compose component for a password text field with validation and visibility toggle.
 *
 * @param password The current value of the password text field.
 * @param onPasswordChange The callback to be invoked when the password value changes.
 * @param errorColor The color to be used for displaying error messages.
 * @param textFieldLabel The label for the password text field.
 * @param errorText The error message to be displayed when the password is not valid.
 */
/*
https://medium.com/@munbonecci/how-to-add-a-password-textfield-component-with-toggle-icon-in-jetpack-compose-46652d771833
 */
// Login page (DOES NOT VALIDATION ERROR PASSWORD)
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
    textFieldLabel: String = "Enter your password",
    errorText: String = "Password not valid",
) {
    // State variables to manage password visibility and validity
    var showPassword by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(true) }

    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary

    // OutlinedTextField for entering user password
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(text = textFieldLabel,
                style = MaterialTheme.typography.labelMedium,
                color = uiColor,
                fontSize = 14.sp
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,

            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),

            cursorColor = MaterialTheme.colorScheme.primary,

            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = uiColor.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(10.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),

        // Keyboard options for password input
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),

        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            // Password visibility toggle icon
            PasswordVisibilityToggleIcon(
                showPassword = showPassword,
                onTogglePasswordVisibility = { showPassword = !showPassword })
        }
    )
}

//        isError = !isPasswordError,
//        supportingText = {
//            // Display error text if the password is not valid
//            if (!isPasswordError) {
//                Text(
//                    modifier = Modifier.fillMaxWidth(),
//                    text = errorText,
//                    color = errorColor
//                )
//            }
//        },
//        label = { Text(textFieldLabel) },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 16.dp)

/**
 * Jetpack Compose component for a password visibility toggle icon.
 *
 * @param showPassword Whether the password is currently visible.
 * @param onTogglePasswordVisibility The callback to toggle password visibility.
 */
@Composable
fun PasswordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    // Determine the icon based on password visibility
    val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val contentDescription = if (showPassword) "Hide password icon" else "Show password icon"

    // IconButton to toggle password visibility
    IconButton(onClick = onTogglePasswordVisibility) {
        Icon(imageVector = image, contentDescription = contentDescription)
    }
}

/**
 * Extension function to check if the [TextFieldValue] represents a valid password.
 */
fun TextFieldValue.isValidPassword(): Boolean {
    val password = text
    val passwordRegex =
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")

    return password.matches((passwordRegex).toRegex())
}


@Preview
@Composable
fun PasswordTextFieldPreview() {
    var password by remember { mutableStateOf("") }
    PasswordTextField(
        password = password,
        onPasswordChange = { password = it },
    )
}

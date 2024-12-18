//package com.example.calorieapp.loginscreens
//
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.ui.semantics.semantics
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.calorieapp.R
//
//
///*
//https://medium.com/@munbonecci/how-to-add-a-password-textfield-component-with-toggle-icon-in-jetpack-compose-46652d771833
// */
//// Login page (DOES NOT VALIDATION ERROR PASSWORD)
//@Composable
//fun PasswordTextField(
//    modifier: Modifier = Modifier,
//    value: String,
//    onValueChange: (String) -> Unit,
//    textFieldLabel: String = "Enter your password",
//) {
//    // State variables to manage password visibility and validity
//    var showPassword by remember { mutableStateOf(false) }
//
//    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
//
//    // OutlinedTextField for entering user password
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = {
//            Text(text = textFieldLabel,
//                style = MaterialTheme.typography.labelMedium,
//                color = uiColor,
//                fontSize = 14.sp
//            )
//        },
//        modifier = modifier
//            .fillMaxWidth()
//            .height(64.dp),
//        colors = TextFieldDefaults.colors(
//            focusedContainerColor = Color.White,
//            unfocusedContainerColor = Color.White,
//            disabledContainerColor = Color.White,
//
//            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
//            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
//
//            cursorColor = MaterialTheme.colorScheme.primary,
//
//            focusedLabelColor = MaterialTheme.colorScheme.primary,
//            unfocusedLabelColor = uiColor.copy(alpha = 0.8f)
//        ),
//        shape = RoundedCornerShape(10.dp),
//        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
//
//        // Keyboard options for password input
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Password,
//            imeAction = ImeAction.Done
//        ),
//
//        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
//        trailingIcon = {
//            // Password visibility toggle icon
//            PasswordVisibilityToggleIcon(
//                showPassword = showPassword,
//                onTogglePasswordVisibility = { showPassword = !showPassword })
//        }
//    )
//}
//
///*
//    Password signup field for signup page
// */
//@Composable
//fun PasswordSignUpTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    textFieldLabel: String = "Enter your password",
//    validPassword: Boolean = false,
//    semanticContentDescription: String = "",
//    onHasStrongPassword: (isStrong: Boolean) -> Unit = {},
//) {
//    // State variables to manage password visibility and validity
//    var showPassword by remember { mutableStateOf(false) }
//    //var isPasswordError by remember { mutableState(true) }
//    var isPasswordError by remember { mutableStateOf(true) }
//
//
//    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
//
//    // OutlinedTextField for entering user password
//    Column(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            // Keyboard options for password input
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Done
//            ),
//            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
//
//            shape = RoundedCornerShape(10.dp),
//            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
//            singleLine = true,
//            //visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
//            trailingIcon = {
//                // Password visibility toggle icon
//                PasswordVisibilityToggleIcon(
//                    showPassword = showPassword,
//                    onTogglePasswordVisibility = { showPassword = !showPassword })
//            },
//            isError = !isPasswordError,
//
//            label = {
//                Text(
//                    text = textFieldLabel,
//                    style = MaterialTheme.typography.labelMedium,
//                    color = uiColor,
//                    fontSize = 14.sp
//                )
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .semantics { contentDescription = semanticContentDescription },
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = Color.White,
//                unfocusedContainerColor = Color.White,
//                disabledContainerColor = Color.White,
//
//                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
//                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
//
//                cursorColor = MaterialTheme.colorScheme.primary,
//
//                focusedLabelColor = MaterialTheme.colorScheme.primary,
//                unfocusedLabelColor = uiColor.copy(alpha = 0.8f)
//            ),
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = stringResource(id = R.string.password_requirement),
//            style = MaterialTheme.typography.bodySmall,
//            color = uiColor,
//            fontSize = 12.sp)
//        // check for strong password
//        if (value.isNotEmpty()) {
//            val strongPassword = isValidPassword(value)
//            onHasStrongPassword(strongPassword)
//
//            Text(
//                modifier = Modifier.semantics { contentDescription = "StrengthPasswordMessage" },
//                text = buildAnnotatedString {
//                    withStyle(
//                        style = SpanStyle(
//                            color = uiColor,
//                            fontSize = 12.sp,
//                        )
//                    ) {
//
//                        append(stringResource(id = R.string.password_level))
//                        withStyle(
//                            style = SpanStyle(
//                                fontSize = 13.sp,
//                                fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
//                                color = if (strongPassword) /*MaterialTheme.colorScheme.primary*/Color(0xFF2B6A46) else MaterialTheme.colorScheme.error,
//                            )
//                        ) {
//                            if (strongPassword) {
//                                append(stringResource(id = R.string.password_valid))
//                            } else {
//                                append(stringResource(id = R.string.password_not_valid))
//
//                            }
//                        }
//                    }
//
//                }
//            )
//        }
//    }
//}
//
//// Not in used
//@Composable
//fun ConfirmPasswordSignUpTextField(
//    value: String,
//    confirmValue: String,
//    onValueChange: (String) -> Unit,
//    textFieldLabel: String = "Repeat your password",
//    errorText: String = "Password not valid",
//    semanticContentDescription: String = "",
//) {
//    // State variables to manage password visibility and validity
//    var showPassword by remember { mutableStateOf(false) }
//    //var isPasswordError by remember { mutableState(true) }
//    var isPasswordError by remember { mutableStateOf(true) }
//
//    var matchingPassword by remember { mutableStateOf(false) }
//
//    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
//
//    Column(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        // OutlinedTextField for entering user password
//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            // Keyboard options for password input
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Done
//            ),
//            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
//
//            shape = RoundedCornerShape(10.dp),
//            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
//            singleLine = true,
//            //visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
//            trailingIcon = {
//                // Password visibility toggle icon
//                PasswordVisibilityToggleIcon(
//                    showPassword = showPassword,
//                    onTogglePasswordVisibility = { showPassword = !showPassword })
//            },
//            isError = !isPasswordError,
//            supportingText = {
//                // Display error text if the password is not valid
//                if (!isPasswordError) {
//                    Text(
//                        modifier = Modifier.fillMaxWidth(),
//                        text = errorText,
//                        color = MaterialTheme.colorScheme.error
//                    )
//                }
//            },
//            label = {
//                Text(
//                    text = textFieldLabel,
//                    style = MaterialTheme.typography.labelMedium,
//                    color = uiColor,
//                    fontSize = 14.sp
//                )
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .semantics { contentDescription = semanticContentDescription },
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = Color.White,
//                unfocusedContainerColor = Color.White,
//                disabledContainerColor = Color.White,
//
//                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
//                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
//
//                cursorColor = MaterialTheme.colorScheme.primary,
//
//                focusedLabelColor = MaterialTheme.colorScheme.primary,
//                unfocusedLabelColor = uiColor.copy(alpha = 0.8f)
//            ),
//        )
//
//        if (confirmValue != value) {
//           Text(
//               text = "Passwords do not match",
//               color = MaterialTheme.colorScheme.error,
//               fontSize = 10.sp,
//               fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
//               modifier = Modifier.semantics {contentDescription = "ConfirmPasswordMessage"},
//           )
//            matchingPassword = true
//        } else {
//            matchingPassword = false
//        }
//    }
//}
///**
// * Jetpack Compose component for a password visibility toggle icon.
// *
// * @param showPassword Whether the password is currently visible.
// * @param onTogglePasswordVisibility The callback to toggle password visibility.
// */
//@Composable
//fun PasswordVisibilityToggleIcon(
//    showPassword: Boolean,
//    onTogglePasswordVisibility: () -> Unit
//) {
//    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
//    // Determine the icon based on password visibility
//    val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
//    val contentDescription = if (showPassword) "Hide password icon" else "Show password icon"
//
//    // IconButton to toggle password visibility
//    IconButton(onClick = onTogglePasswordVisibility) {
//        Icon(imageVector = image,
//            contentDescription = contentDescription,
//            tint = uiColor
//        )
//    }
//}
//
///**
// * Extension function to check if the password: [String] represents a valid password.
// */
//private fun isValidPassword(password: String): Boolean {
//    val strongPasswordRegex = Regex(
//        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
//    )
//    return password.matches(strongPasswordRegex)
//}
//
//
//
//@Preview
//@Composable
//fun PasswordTextFieldPreview() {
//    var password by remember { mutableStateOf("123") }
//    PasswordTextField(
//        value = password,
//        onValueChange = { password = it },
//    )
//}
//
//@Preview
//@Composable
//fun PasswordSignUpTextFieldPreview() {
//    var password by remember { mutableStateOf("123") }
//    PasswordSignUpTextField(
//        value = password,
//        onValueChange = { password = it }
//    )
//}

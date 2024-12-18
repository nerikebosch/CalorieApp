package com.example.calorieapp.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calorieapp.loginscreens.PasswordVisibilityToggleIcon
import com.example.calorieapp.R.string as AppText

@Composable
fun BasicField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(text)) }
    )
}

@Composable
fun EmailField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary

    // Adjust the icon tint for visibility in both light and dark themes
    if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onPrimary // Dark mode: use a light icon color
    } else {
        MaterialTheme.colorScheme.onSurface // Light mode: use a dark icon color
    }
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        label = {
            Text(
                text = stringResource(AppText.email),
                style = MaterialTheme.typography.labelMedium,
                color = uiColor,
                fontSize = 14.sp
            )
        },
        colors = TextFieldDefaults.colors(
            // Text colors
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledTextColor = Color.Gray,

            // Container colors
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,

            // Border/Indicator colors
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),

            // Label colors
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = uiColor.copy(alpha = 0.8f),

            // Cursor color
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(10.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),

        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
    )
}


@Composable
fun PasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PasswordField(value, AppText.password, onNewValue, modifier)
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textFieldLabel: String = "Enter your password",
) {
    // State variables to manage password visibility and validity
    var showPassword by remember { mutableStateOf(false) }

    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary

    // OutlinedTextField for entering user password
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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

@Composable
fun RepeatPasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PasswordField(value, AppText.repeat_password, onNewValue, modifier)
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPassword by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

//    val icon =
//        if (isVisible) painterResource(AppIcon.ic_visibility_on)
//        else painterResource(AppIcon.ic_visibility_off)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            PasswordVisibilityToggleIcon(
                showPassword = showPassword,
                onTogglePasswordVisibility = { showPassword = !showPassword })
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    trailing: @Composable (() -> Unit)? = null,
) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary

    // Adjust the icon tint for visibility in both light and dark themes
    if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onPrimary // Dark mode: use a light icon color
    } else {
        MaterialTheme.colorScheme.onSurface // Light mode: use a dark icon color
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = uiColor,
                fontSize = 14.sp
            )
        },
        trailingIcon = {
            // Check if trailing composable is provided
            trailing?.invoke()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = TextFieldDefaults.colors(
            // Text colors
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledTextColor = Color.Gray,

            // Container colors
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,

            // Border/Indicator colors
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),

            // Label colors
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = uiColor.copy(alpha = 0.8f),

            // Cursor color
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(10.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),

        // Keyboard options for text
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )

}

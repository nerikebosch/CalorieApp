//package com.example.calorieapp.loginscreens
//
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LoginTextField(
//    modifier: Modifier = Modifier,
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    trailing: @Composable (() -> Unit)? = null,
//    isError: Boolean = false,
//    errorMessage: String? = null,
//) {
//    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
//
//    // Adjust the icon tint for visibility in both light and dark themes
//    if (isSystemInDarkTheme()) {
//        MaterialTheme.colorScheme.onPrimary // Dark mode: use a light icon color
//    } else {
//        MaterialTheme.colorScheme.onSurface // Light mode: use a dark icon color
//    }
//
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = {
//            Text(
//                text = label,
//                style = MaterialTheme.typography.labelMedium,
//                color = uiColor,
//                fontSize = 14.sp
//            )
//        },
//        trailingIcon = {
//            // Check if trailing composable is provided
//            trailing?.invoke()
//        },
//        modifier = modifier
//            .fillMaxWidth()
//            .height(64.dp),
//        isError = isError,
//        colors = TextFieldDefaults.colors(
//            // Text colors
//            focusedTextColor = Color.Black,
//            unfocusedTextColor = Color.Black,
//            disabledTextColor = Color.Gray,
//
//            // Container colors
//            focusedContainerColor = Color.White,
//            unfocusedContainerColor = Color.White,
//            disabledContainerColor = Color.White,
//
//            // Border/Indicator colors
//            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
//            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
//
//            // Label colors
//            focusedLabelColor = MaterialTheme.colorScheme.primary,
//            unfocusedLabelColor = uiColor.copy(alpha = 0.8f),
//
//            // Cursor color
//            cursorColor = MaterialTheme.colorScheme.primary
//        ),
//        shape = RoundedCornerShape(10.dp),
//        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
//
//        // Keyboard options for text
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Email,
//            imeAction = ImeAction.Next
//        )
//    )
//
//    // Show error message below the text field if applicable
//    if (isError && errorMessage != null) {
//        Text(
//            text = errorMessage,
//            color = MaterialTheme.colorScheme.error,
//            style = MaterialTheme.typography.bodySmall,
//            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TestOutlinedTextField() {
//    val uiColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary
//    OutlinedTextField(
//        value = "Test",
//        onValueChange = {},
//        label = { Text("Label") },
//        colors = TextFieldDefaults.colors(
//            // Text colors
//            focusedTextColor = Color.Black,
//            unfocusedTextColor = Color.Black,
//            disabledTextColor = Color.Gray,
//
//            // Container colors
//            focusedContainerColor = Color.White,
//            unfocusedContainerColor = Color.White,
//            disabledContainerColor = Color.White,
//
//            // Border/Indicator colors
//            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
//            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
//
//            // Label colors
//            focusedLabelColor = MaterialTheme.colorScheme.primary,
//            unfocusedLabelColor = uiColor.copy(alpha = 0.8f),
//
//            // Cursor color
//            cursorColor = MaterialTheme.colorScheme.primary
//        ),
//    )
//}
//

package com.example.calorieapp.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * A basic text button composable that displays text and triggers an action when clicked.
 *
 * @param text The string resource ID for the button text.
 * @param modifier The modifier to be applied to the button.
 * @param action The action to be triggered when the button is clicked.
 */
@Composable
fun BasicTextButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    TextButton(onClick = action, modifier = modifier) { Text(text = stringResource(text)) }
}


/**
 * A basic button composable that displays text and triggers an action when clicked.
 * The button uses the primary container color from the Material theme.
 *
 * @param text The string resource ID for the button text.
 * @param modifier The modifier to be applied to the button.
 * @param action The action to be triggered when the button is clicked.
 */
@Composable
fun BasicButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    Button(
        onClick = action,
        modifier = modifier,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Text(text = stringResource(text), fontSize = 16.sp)
    }
}


/**
 * A login button composable that is typically used in login screens.
 * The button has a fixed height, rounded corners, and uses the primary color from the Material theme.
 *
 * @param text The string resource ID for the button text.
 * @param modifier The modifier to be applied to the button.
 * @param action The action to be triggered when the button is clicked.
 */
@Composable
fun LoginButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    Button(
        onClick = action,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 4.dp)
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}


/**
 * A confirmation button composable typically used in dialogs.
 * The button uses the primary color from the Material theme.
 *
 * @param text The string resource ID for the button text.
 * @param action The action to be triggered when the button is clicked.
 */
@Composable
fun DialogConfirmButton(@StringRes text: Int, action: () -> Unit) {
    Button(
        onClick = action,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = stringResource(text))
    }
}


/**
 * A cancellation button composable typically used in dialogs.
 * The button uses the primary color from the Material theme.
 *
 * @param text The string resource ID for the button text.
 * @param action The action to be triggered when the button is clicked.
 */
@Composable
fun DialogCancelButton(@StringRes text: Int, action: () -> Unit) {
    Button(
        onClick = action,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {
        Text(text = stringResource(text))
    }
}

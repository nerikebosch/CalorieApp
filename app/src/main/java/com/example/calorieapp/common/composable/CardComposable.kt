package com.example.calorieapp.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


/**
 * A composable function for displaying a card editor with a dangerous (primary) highlight color.
 * This is typically used for actions that require attention, such as editing critical information.
 *
 * @param title The string resource ID for the title of the card.
 * @param icon The icon to display on the card, represented as an [ImageVector].
 * @param content The text content to display on the card.
 * @param modifier The modifier to be applied to the card.
 * @param onEditClick The action to be triggered when the card is clicked.
 */
@ExperimentalMaterial3Api
@Composable
fun DangerousCardEditor(
    @StringRes title: Int,
    //@DrawableRes icon: Int,
    icon: ImageVector,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.primary, modifier)
}


/**
 * A composable function for displaying a card editor with a regular (onSurface) highlight color.
 * This is typically used for less critical editing actions.
 *
 * @param title The string resource ID for the title of the card.
 * @param icon The icon to display on the card, represented as an [ImageVector].
 * @param content The text content to display on the card.
 * @param modifier The modifier to be applied to the card.
 * @param onEditClick The action to be triggered when the card is clicked.
 */
@ExperimentalMaterial3Api
@Composable
fun RegularCardEditor(
    @StringRes title: Int,
    //@DrawableRes icon: Int,
    icon: ImageVector,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.onSurface, modifier)
}


/**
 * A private composable function that serves as the base implementation for card editors.
 * It displays a title, content, and an icon, and applies a highlight color to the title and icon.
 *
 * @param title The string resource ID for the title of the card.
 * @param icon The icon to display on the card, represented as an [ImageVector].
 * @param content The text content to display on the card.
 * @param onEditClick The action to be triggered when the card is clicked.
 * @param highlightColor The color to use for highlighting the title and icon.
 * @param modifier The modifier to be applied to the card.
 */
@ExperimentalMaterial3Api
@Composable
private fun CardEditor(
    @StringRes title: Int,
    //@DrawableRes icon: Int,
    icon: ImageVector,
    content: String,
    onEditClick: () -> Unit,
    highlightColor: Color,
    modifier: Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier,
        onClick = onEditClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) { Text(stringResource(title), color = highlightColor) }

            if (content.isNotBlank()) {
                Text(text = content,
                    modifier = Modifier.padding(16.dp, 0.dp),
                style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(
                imageVector = icon,
                contentDescription = "Icon",
                tint = highlightColor)
        }
    }
}


/**
 * A composable function for displaying a card with a dropdown selector.
 * This is typically used for selecting options from a list.
 *
 * @param label The string resource ID for the label of the dropdown.
 * @param options The list of options to display in the dropdown.
 * @param selection The currently selected option.
 * @param modifier The modifier to be applied to the card.
 * @param onNewValue The action to be triggered when a new value is selected.
 */
@Composable
@ExperimentalMaterial3Api
fun CardSelector(
    @StringRes label: Int,
    options: List<String>,
    selection: String,
    modifier: Modifier = Modifier,
    onNewValue: (String) -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        DropdownSelector(
            label = label,
            options = options,
            selection = selection,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            onNewValue = onNewValue
        )
    }
}

/**
 * A composable function for displaying a card editor with text content and an icon.
 * This is typically used for displaying and editing text-based information.
 *
 * @param label The label text to display on the card.
 * @param detail The detail text to display on the card.
 * @param icon The icon to display on the card, represented as an [ImageVector].
 * @param onEditClick The action to be triggered when the card is clicked.
 * @param modifier The modifier to be applied to the card.
 */
@ExperimentalMaterial3Api
@Composable
fun TextCardEditor(
    label: String,
    detail: String,
    icon: ImageVector,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val highlightColor = MaterialTheme.colorScheme.onSurface

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier,
        onClick = onEditClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (label.isNotBlank()) {
                    Text(label, color = highlightColor)
                }

                if (detail.isNotBlank()) {
                    Text(
                        text = detail,
                        modifier = Modifier.padding(top = 2.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Icon(
                imageVector = icon,
                contentDescription = "Icon",
                tint = highlightColor)
        }
    }
}
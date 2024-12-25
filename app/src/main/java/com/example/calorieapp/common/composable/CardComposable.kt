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
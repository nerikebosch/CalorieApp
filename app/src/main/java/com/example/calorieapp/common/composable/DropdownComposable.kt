package com.example.calorieapp.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource


/**
 * A composable function that creates a context menu with a list of options.
 * When an option is selected, the provided [onActionClick] callback is invoked.
 *
 * @param options The list of options to display in the dropdown menu.
 * @param modifier The modifier to be applied to the dropdown menu container.
 * @param onActionClick A callback that is invoked when an option is selected. The selected option is passed as a parameter.
 */
@Composable
@ExperimentalMaterial3Api
fun DropdownContextMenu(
    options: List<String>,
    modifier: Modifier = Modifier,
    onActionClick: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { isExpanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        isExpanded = false
                        onActionClick(selectionOption)
                    }
                )
            }
        }
    }
}


/**
 * A composable function that creates a dropdown selector with a list of options.
 * The selected option is displayed in a text field, and when a new option is selected,
 * the provided [onNewValue] callback is invoked.
 *
 * @param label The resource ID of the label to display above the dropdown selector.
 * @param options The list of options to display in the dropdown menu.
 * @param selection The currently selected option.
 * @param modifier The modifier to be applied to the dropdown selector container.
 * @param onNewValue A callback that is invoked when a new option is selected. The selected option is passed as a parameter.
 */
@Composable
@ExperimentalMaterial3Api
fun DropdownSelector(
    @StringRes label: Int,
    options: List<String>,
    selection: String,
    modifier: Modifier = Modifier,
    onNewValue: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        modifier = modifier,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            modifier = Modifier
                //.menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selection,
            onValueChange = {},
            label = { Text(stringResource(label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        onNewValue(selectionOption)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
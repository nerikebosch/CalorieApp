package com.example.calorieapp.screens.adddata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calorieapp.R
import com.example.calorieapp.R.string as AppText
import com.example.calorieapp.common.composable.DialogCancelButton
import com.example.calorieapp.common.composable.DialogConfirmButton
import com.example.calorieapp.model.Product


/**
 * Composable function to display an elevated card with a title and an optional content area.
 *
 * @param title The title of the card.
 * @param modifier Modifier for layout customization.
 * @param content Optional composable lambda to add additional UI elements (e.g., icons, images).
 * @param onClick Lambda function to handle click events.
 */
@Composable
fun ElevatedCardAddDataScreen(
    title: String, // Title of the card
    modifier: Modifier = Modifier, // Custom modifier for layout
    content: @Composable (() -> Unit)? = null, // Lambda that can accept other composable (like icons, images)
    onClick: () -> Unit = {} // Lambda to handle click event
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .size(width = 300.dp, height = 60.dp) // Adjust size as necessary
    ) {
        // Using Column for stacking items vertically (text + icon/content)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // Padding inside the card
                horizontalArrangement = Arrangement.SpaceBetween

        ) {
            // Title text at the top
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(bottom = 8.dp) // Space between title and content

            )


            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { onClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
                )
            }

            // Optional content: Show any composable passed into the content lambda
            content?.invoke() // If content is provided, display it here
        }
    }
}


/**
 * Composable function to display a filled card with a title, expandable list of user products, and delete functionality.
 *
 * @param title The title of the card.
 * @param userProducts List of user-added products to be displayed.
 * @param onDeleteProduct Callback function to handle product deletion.
 */
@Composable
fun FilledCardExample(
    title : String,
    modifier: Modifier = Modifier,
    userProducts: List<Product>,
    onDeleteProduct: (Product) -> Unit,
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    var showWarningDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )

            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Show less" else "Show more"
                )
            }
        }

        if (expanded) {
            Column {
                userProducts.forEach { product ->
                    ListItem(
                        headlineContent = { product.productName?.let { Text(it) } },
                        supportingContent = {
                            Column {
                                Text("Carbohydrates: ${product.nutrients?.carbohydrates ?: "N/A"} g")
                                Text("Protein: ${product.nutrients?.protein ?: "N/A"} g")
                                Text("Fat: ${product.nutrients?.fat ?: "N/A"} g")
                                Text("Calories: ${product.nutrients?.calories ?: "N/A"} kcal")
                            } },
                        trailingContent = {
                            IconButton(
                                onClick = {
                                    productToDelete = product
                                    showWarningDialog = true
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ){
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    )
                }
            }
        }

        if (showWarningDialog && productToDelete != null) {
            AlertDialog(
                title = { Text(stringResource(AppText.delete_product_title)) },
                text = { Text(stringResource(AppText.delete_product_description)) },
                dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
                confirmButton = {
                    DialogConfirmButton(AppText.delete) {
                        onDeleteProduct(productToDelete!!)
                        showWarningDialog = false
                    }
                },
                onDismissRequest = { showWarningDialog = false }
            )
        }
    }
}
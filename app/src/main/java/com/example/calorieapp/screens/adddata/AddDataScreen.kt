package com.example.calorieapp.screens.adddata

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.model.Nutrients
import com.example.calorieapp.model.Product
import com.example.calorieapp.api.RetrofitClient
import com.example.calorieapp.common.composable.TextActionToolbar
import kotlinx.coroutines.launch
import com.example.calorieapp.R.string as AppText


@Composable
fun AddDataScreen(
    openAndPopUp: (String, String) -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: AddDataViewModel = hiltViewModel()

){
    AddDataSelection(
        onSaveClick = {selectedProducts ->
            sharedViewModel.setUserProducts("Breakfast", selectedProducts)
            viewModel.onSaveClick(openAndPopUp) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataSelection(
    onSaveClick: (List<Product>) -> Unit = {}
) {
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // API integration variables
    val api = RetrofitClient.api

    var suggestions by remember { mutableStateOf(listOf<Product>()) }
    var searchQuery by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) } // Loading state
    var errorMessage by remember { mutableStateOf<String?>(null) } // Error message state
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val products = remember { mutableStateListOf<Product>() }
    val userProducts = remember { mutableStateListOf<List<Product>>() }

    // State to track checked items (if the checkbox is selected)
    val checkedItems by remember { mutableStateOf(mutableListOf<String>()) }

    Surface {
        Column(modifier = Modifier.fillMaxSize()){

            TextActionToolbar(
                title = AppText.select_product,
                text = "Save",
                modifier = Modifier,
                endAction = {onSaveClick(products.toList())}
            )

            Spacer(modifier = Modifier.height(16.dp))

            SearchBar(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchQuery,
                        onQueryChange = { query ->
                            searchQuery = query
                            errorMessage = null // Clear error message on new query
                            if (query.isNotEmpty()) {
                                coroutineScope.launch {
                                    loading = true // Start loading
                                    try {
                                        val response = api.searchProducts(searchTerms = query)
                                        suggestions = response.products
                                        if (response.products.isEmpty()) {
                                            errorMessage = "No products found."
                                        }
                                    } catch (e: Exception) {
                                        suggestions = emptyList() // Clear suggestions
                                        errorMessage = "An error occurred. Please try again."
                                    } finally {
                                        loading = false // Stop loading
                                    }
                                }
                            } else {
                                suggestions = emptyList() // Clear suggestions if search query is empty
                            }
                        },
                        onSearch = { expanded = false },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("Search for food products...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (textFieldState.text.isNotEmpty()) {
                                IconButton(onClick = {
                                    textFieldState.setTextAndPlaceCursorAtEnd("")
                                    suggestions = emptyList() // Clear suggestions
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = null)
                                }
                            }
                        },
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                // Suggestions list based on API response
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    if (loading) {
                        // Show a loading indicator while fetching data
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else if (errorMessage != null) {
                        // Show error message if any
                        Text(
                            text = errorMessage ?: "",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                        )
                    } else {
                        suggestions.forEach { product ->
                            val productName = product.productName ?: "Unknown Product"
                            val isChecked = checkedItems.contains(productName) // Check if the item is selected

                            ListItem(
                                headlineContent = { Text(productName) },
                                leadingContent = {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { isChecked ->
                                            // Add or remove the product from the checkedItems list based on its checked state
                                            if (isChecked) {
                                                products.add(product)
                                            } else {
                                                products.remove(product)
                                            }
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .clickable {
                                        // Set the selected product when clicked
                                        selectedProduct = product
                                    }

                            )
                        }
                    }
                }
            }

            // Static content below the search bar
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 72.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.semantics { traversalIndex = 1f },
            ) {
                items(suggestions) { product ->
                    // Extract the product name or show a fallback if it's null
                    val productName = product.productName ?: "Unknown Product"

                    // Display each product as a list item
                    ListItem(
                        headlineContent = { Text(productName) },
                        leadingContent = { Icon(Icons.Default.Star, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }


        }
    }
}


@Composable
fun NutritionalInfo(nutrients: Nutrients?) {
    if (nutrients != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nutritional Information", style = MaterialTheme.typography.bodyMedium)

            Text("Carbohydrates: ${nutrients.carbohydrates?.let { "$it g" } ?: "N/A"}")
            Text("Proteins: ${nutrients.protein?.let { "$it g" } ?: "N/A"}")
            Text("Fat: ${nutrients.fat?.let { "$it g" } ?: "N/A"}")
            Text("Calories: ${nutrients.calories?.let { "$it kcal" } ?: "N/A"}")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddDataPreview() {
    AddDataSelection()
}


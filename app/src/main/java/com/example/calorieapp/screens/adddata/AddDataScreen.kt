

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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calorieapp.screens.adddata.MealTimeSelection
import com.example.calorieapp.screens.adddata.Nutrients
import com.example.calorieapp.screens.adddata.Product
import com.example.calorieapp.screens.adddata.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarSample() {
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

    Surface {
        Box(Modifier.fillMaxSize().semantics { isTraversalGroup = true }) {
            SearchBar(
                modifier = Modifier.align(Alignment.TopCenter).semantics { traversalIndex = 0f },
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
                        // Show suggestions if available
                        suggestions.forEach { product ->
                            val productName = product.productName ?: "Unknown Product"
                            ListItem(
                                headlineContent = { Text(productName) },
                                leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                modifier =
                                Modifier.clickable {
                                    textFieldState.setTextAndPlaceCursorAtEnd(productName)
                                    selectedProduct = product
                                    expanded = false
                                    errorMessage = null // Clear error message on selection
                                    suggestions = emptyList() // Clear suggestions after selection
                                }
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
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
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            selectedProduct?.let { product ->
                NutritionalInfo(product.nutrients)
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
    SearchBarSample()
}


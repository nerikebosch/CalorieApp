package com.example.calorieapp.screens.adddata

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorieapp.CalorieAppState
import com.example.calorieapp.model.Nutrients
import com.example.calorieapp.model.Product
import com.example.calorieapp.api.RetrofitClient
import com.example.calorieapp.common.composable.TextActionToolbar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import com.example.calorieapp.R.string as AppText


@Composable
fun AddDataScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: AddDataViewModel = hiltViewModel(),
    mealName: String,
    date: String,
) {
    // State collection
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSelectedProducts(mealName, date)
    }

    AddDataContent(
        uiState = uiState,
        mealName = mealName,
        onProductSelected = { product, isSelected ->
            if (isSelected) viewModel.addProduct(product) else viewModel.removeProduct(product)
        },
        onSaveClick = {
            viewModel.onSaveClick(
                mealName = mealName,
                date = date,
                openAndPopUp = openAndPopUp
            )
        },
        onSearchQueryChange = viewModel::updateSearchQuery
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddDataContent(
    uiState: AddDataUiState,
    mealName: String,
    onProductSelected: (Product, Boolean) -> Unit,
    onSaveClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TextActionToolbar(
                title = AppText.select_product,
                text = "Save",
                modifier = Modifier,
                endAction = onSaveClick
            )
            Spacer(modifier = Modifier.height(16.dp))

            SearchSection(
                uiState = uiState,
                onProductSelected = onProductSelected,
                onSearchQueryChange = onSearchQueryChange
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun SearchSection(
    uiState: AddDataUiState,
    onProductSelected: (Product, Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query = uiState.searchQuery,
                onQueryChange = { query ->
                    onSearchQueryChange(query)
                    //if (query.isNotEmpty()) onSearchProducts(query)
                },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search for food products...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = if (uiState.searchQuery.isNotEmpty()) {
                     {
                         IconButton(onClick = { onSearchQueryChange("") }) {
                         Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                } else null
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        SearchResults(
            uiState = uiState,
            onProductSelected = onProductSelected
        )
    }
}

@Composable
private fun SearchResults(
    uiState: AddDataUiState,
    onProductSelected: (Product, Boolean) -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.errorMessage != null -> ErrorMessage(uiState.errorMessage)
            else -> SuggestionsList(
                suggestions = uiState.suggestions,
                selectedProducts = uiState.selectedProducts,
                onProductSelected = onProductSelected
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun SuggestionsList(
    suggestions: List<Product>,
    selectedProducts: List<Product>,
    onProductSelected: (Product, Boolean) -> Unit
) {
    suggestions.forEach { product ->
        val isSelected = selectedProducts.contains(product)
        ListItem(
            headlineContent = {
                Text(product.productName ?: "Unknown Product")
            },
            leadingContent = {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { checked ->
                        onProductSelected(product, checked)
                    }
                )
            }
        )
    }
}

@Composable
private fun SelectedProductsList(selectedProducts: List<Product>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(selectedProducts) { product ->
            ListItem(
                headlineContent = {
                    Text(product.productName ?: "Unknown Product")
                },
                supportingContent = {
                    product.nutrients?.let { NutritionalInfo(it) }
                }
            )
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

}


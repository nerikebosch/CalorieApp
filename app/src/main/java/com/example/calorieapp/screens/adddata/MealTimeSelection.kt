package com.example.calorieapp.screens.adddata

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calorieapp.R
import com.example.calorieapp.common.composable.DialogCancelButton
import com.example.calorieapp.common.composable.DialogConfirmButton
import com.example.calorieapp.model.MealData
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.Nutrients
import com.example.calorieapp.model.Product
import com.example.calorieapp.model.UserProducts
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.calorieapp.R.string as AppText


/**
 * Composable function for displaying the Meal Time screen.
 *
 * @param openScreen Function to navigate to another screen.
 * @param openAndPopUp Function to navigate and remove the current screen from the back stack.
 * @param viewModel ViewModel that manages the Meal Time screen logic.
 */
@Composable
fun MealTimeScreen(
    openScreen: (String) -> Unit,
    openAndPopUp: (String, String) -> Unit,
    viewModel: MealTimeViewModel = hiltViewModel()
) {
    var selectedDate by remember { mutableLongStateOf(Calendar.getInstance().timeInMillis) }

    // Collect as UserProducts? instead of List<UserProducts>
    val userProducts by viewModel.userProducts.collectAsStateWithLifecycle(initialValue = null)


    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val formattedDate = remember(selectedDate) { dateFormatter.format(Date(selectedDate)) }

    var showDeleteAllDialog by remember { mutableStateOf(false) }

    println("MealTimeScreenDebug: Formatted Date: $formattedDate")
    println("MealTimeScreenDebug: UserProducts initial: $userProducts")


    LaunchedEffect(formattedDate) {
        viewModel.loadUserProductsForDate(formattedDate)
        println("MealTimeScreenDebug: LaunchedEffect called with date: $formattedDate")
    }

    MealTimeSelection(
        selectedDate = selectedDate,
        onDateSelected = { newDate ->
            selectedDate = newDate
            viewModel.setSelectedDate(newDate)
        },
        userProducts = userProducts,
        onBreakfastClick = { viewModel.onBreakfastClick(openAndPopUp) },
        onLunchClick = { viewModel.onLunchClick(openAndPopUp) },
        onDinnerClick = { viewModel.onDinnerClick(openAndPopUp) },
        onSnackClick = { viewModel.onSnackClick(openAndPopUp) },
        onDeleteProduct = { mealName, product ->
            viewModel.onDeleteProduct(mealName, product)
        },
        onDeleteAllProducts = {
           // viewModel.onDeleteAllProducts()
            showDeleteAllDialog = true
        }
    )
    if (showDeleteAllDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.delete_product_title)) },
            text = { Text(stringResource(AppText.delete_all_product_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showDeleteAllDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.delete) {
                    viewModel.onDeleteAllProducts()
                    showDeleteAllDialog = false
                }
            },
            onDismissRequest = { showDeleteAllDialog = false }
        )

    }
}


/**
 * Composable function for selecting and displaying meals for a selected date.
 *
 * @param selectedDate The currently selected date.
 * @param onDateSelected Callback for when a new date is selected.
 * @param userProducts The user's products for the selected date.
 * @param onBreakfastClick Callback for breakfast selection.
 * @param onLunchClick Callback for lunch selection.
 * @param onDinnerClick Callback for dinner selection.
 * @param onSnackClick Callback for snack selection.
 * @param onDeleteProduct Callback for deleting a specific product from a meal.
 * @param onDeleteAllProducts Callback for deleting all products for the selected date.
 */
@Composable
fun MealTimeSelection(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    userProducts: UserProducts? = UserProducts(),
    onBreakfastClick: () -> Unit,
    onLunchClick: () -> Unit,
    onDinnerClick: () -> Unit,
    onSnackClick: () -> Unit,
    onDeleteProduct: (MealName, Product) -> Unit,
    onDeleteAllProducts: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()) } // Include year
    val formattedDate = remember(selectedDate) { dateFormatter.format(Date(selectedDate)) }

    Surface(modifier = Modifier.fillMaxSize()) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                DateSelector(
                    selectedDate = selectedDate,
                    formattedDate = formattedDate,
                    onDateSelected = onDateSelected,
                    onDeleteAllProducts = onDeleteAllProducts
                )
            }


            items(MealName.entries.toTypedArray()) { mealName ->
                   ElevatedCardAddDataScreen(
                       title = mealName.name,
                       modifier = Modifier.fillMaxWidth(),
                       onClick = {
                           when (mealName) {
                               MealName.Breakfast -> onBreakfastClick()
                               MealName.Lunch -> onLunchClick()
                               MealName.Dinner -> onDinnerClick()
                               MealName.Snack -> onSnackClick()
                           }
                       }
                   )

                val products = when (mealName) {
                    MealName.Breakfast -> userProducts?.breakfast?.products
                    MealName.Lunch -> userProducts?.lunch?.products
                    MealName.Dinner -> userProducts?.dinner?.products
                    MealName.Snack -> userProducts?.snacks?.products
                } ?: emptyList()

                    FilledCardExample(
                        title = mealName.name,
                        modifier = Modifier.padding(top = 8.dp),
                        userProducts = products,
                        onDeleteProduct = { product -> onDeleteProduct(mealName, product) }
                    )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}



/**
 * Composable function for selecting a date and clearing all products.
 *
 * @param selectedDate The currently selected date in milliseconds.
 * @param formattedDate The formatted string representation of the selected date.
 * @param onDateSelected Callback function to handle date selection.
 * @param onDeleteAllProducts Callback function to handle clearing all products.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    selectedDate: Long,
    formattedDate: String,
    onDateSelected: (Long) -> Unit,
    onDeleteAllProducts: () -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    calendar.timeInMillis = selectedDate

    TopAppBar(
        title = {
            TextButton(
                onClick = {
                    val datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date")
                        .setSelection(calendar.timeInMillis)
                        .build()

                    datePicker.addOnPositiveButtonClickListener { timestamp ->
                        onDateSelected(timestamp)
                    }

                    datePicker.show(
                        (context as AppCompatActivity).supportFragmentManager,
                        "datePicker"
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                onDateSelected(calendar.timeInMillis)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Previous Date"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                onDateSelected(calendar.timeInMillis)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Next Date"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.End  // Aligns to the right
    ) {
        TextButton(
            onClick = { onDeleteAllProducts() }
        ) {
            Text(
                text = "Clear All",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary // Use primary color for visibility
            )
        }
    }

}

/**
 * Preview function for the MealTimeScreen, displaying sample products and meals.
 */
@SuppressLint("SuspiciousIndentation")
@Preview(showBackground = true)
@Composable
fun MealTimeScreenPreview() {
    val sampleProductList = listOf(
        Product(
            productName = "Apple",
            nutrients = Nutrients(calories = 52.0)
        ),
        Product(
            productName = "Banana",
            nutrients = Nutrients(calories = 89.0)
        ),
        Product(
            productName = "Orange",
            nutrients = Nutrients(calories = 47.0)
        ),
        Product(
            productName = "Yogurt",
            nutrients = Nutrients(calories = 60.0, protein = 8.0)
        ),
        Product(
            productName = "Oatmeal",
            nutrients = Nutrients(calories = 60.0, carbohydrates = 27.0)
        ),
        Product(
            productName = "Chicken Breast",
            nutrients = Nutrients(calories = 165.0, protein = 31.0)
        ),
        Product(
            productName = "Broccoli",
            nutrients = Nutrients(calories = 34.0, carbohydrates = 7.0)
        ),
        Product(
            productName = "Brown Rice",
            nutrients = Nutrients(calories  = 216.0, carbohydrates = 45.0)
        ),
        Product(
            productName = "Salmon",
            nutrients = Nutrients(calories = 208.0, protein = 22.0)
        ),
        Product(
            productName = "Eggs",
            nutrients = Nutrients(calories = 78.0, protein = 6.0)
        )
    )

    val sampleUserProducts = UserProducts(
        date = formatDateToString(System.currentTimeMillis()),
        breakfast = MealData(MealName.Breakfast, sampleProductList.subList(2,4)),
        lunch = MealData(MealName.Lunch, sampleProductList), // Example with fewer products
        dinner = MealData(MealName.Dinner, sampleProductList.subList(3,5)),
        snacks = MealData(MealName.Snack, sampleProductList.subList(5,6))
    )


        MealTimeSelection(
            selectedDate = System.currentTimeMillis(),
            onDateSelected = {}, // Empty lambda for preview
            userProducts = sampleUserProducts,
            onBreakfastClick = {},
            onLunchClick = {},
            onDinnerClick = {},
            onSnackClick = {},
            onDeleteProduct = { _, _ -> },
            onDeleteAllProducts = {}
        )

}
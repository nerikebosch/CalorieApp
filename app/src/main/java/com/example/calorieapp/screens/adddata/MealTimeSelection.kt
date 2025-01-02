package com.example.calorieapp.screens.adddata

import android.app.LauncherActivity
import android.graphics.pdf.models.ListItem
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.R
import com.example.calorieapp.model.Product
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealTimeScreen(
    openScreen: (String) -> Unit,
    openAndPopUp: (String, String) -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: MealTimeViewModel = hiltViewModel()
) {
    val userProducts by sharedViewModel.userProducts.collectAsState()

    MealTimeSelection(
        userProducts = userProducts,
        onTabSelected = openScreen,
        onBreakfastClick = { viewModel.onBreakfastClick(openAndPopUp) },
        onLunchClick = { viewModel.onLunchClick(openAndPopUp) },
        onDinnerClick = { viewModel.onDinnerClick(openAndPopUp) },
        onSnackClick = { viewModel.onSnackClick(openAndPopUp) }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealTimeSelection(
    userProducts: Map<String, List<Product>> = emptyMap(),
    onTabSelected: (String) -> Unit = {},
    onBreakfastClick: () -> Unit = {},
    onLunchClick: () -> Unit = {},
    onDinnerClick: () -> Unit = {},
    onSnackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
    val calendar = remember { mutableStateOf(Calendar.getInstance()) }
    val selectedDate = remember { mutableStateOf(dateFormatter.format(calendar.value.time)) }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                TopAppBar(
                    title = {
                        TextButton(
                            onClick = {
                                // Open Date Picker
                                val datePicker = MaterialDatePicker.Builder.datePicker()
                                    .setTitleText("Select Date")
                                    .setSelection(calendar.value.timeInMillis)
                                    .build()

                                datePicker.addOnPositiveButtonClickListener { timestamp ->
                                    calendar.value.timeInMillis = timestamp
                                    selectedDate.value = dateFormatter.format(calendar.value.time)
                                }

                                datePicker.show(
                                    (context as AppCompatActivity).supportFragmentManager,
                                    "datePicker"
                                )
                            },
                            modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = selectedDate.value,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            calendar.value.add(Calendar.DAY_OF_MONTH, -1)
                            selectedDate.value = dateFormatter.format(calendar.value.time)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = "Previous Date"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            calendar.value.add(Calendar.DAY_OF_MONTH, 1)
                            selectedDate.value = dateFormatter.format(calendar.value.time)
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
            }

            // Add ElevatedCard for each meal time
            items(listOf("Breakfast", "Lunch", "Dinner", "Snack")) { mealTime ->
                ElevatedCardAddDataScreen(
                    title = mealTime,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        when (mealTime) {
                            "Breakfast" -> onBreakfastClick()
                            "Lunch" -> onLunchClick()
                            "Dinner" -> onDinnerClick()
                            "Snack" -> onSnackClick()
                        }
                    }
                )

                FilledCardExample(
                    title = "",
                    modifier = Modifier.fillMaxWidth(),
                    userProducts = userProducts[mealTime] ?: emptyList()
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MealTimePreview() {
    MealTimeSelection()
}

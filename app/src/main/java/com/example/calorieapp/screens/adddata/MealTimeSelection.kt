package com.example.calorieapp.screens.adddata

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealTimeScreen(
    openScreen: (String) -> Unit,
    viewModel: MealTimeViewModel = hiltViewModel()
) {
    MealTimeSelection(onTabSelected = openScreen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealTimeSelection(
    onTabSelected: (String) -> Unit = {}
){
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
    val calendar = remember { mutableStateOf(Calendar.getInstance()) }

    val selectedDate = remember { mutableStateOf(dateFormatter.format(calendar.value.time)) }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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

            // Main Content for meals

            ElevatedCardAddDataScreen(
                title = "Breakfast",
                modifier = Modifier.fillMaxWidth()
            )

            FilledCardExample(
                title = "",
                modifier = Modifier.fillMaxWidth()
            )

            ElevatedCardAddDataScreen(
                title = "Lunch",
                modifier = Modifier.fillMaxWidth()
            )

            FilledCardExample(
                title = "",
                modifier = Modifier.fillMaxWidth()
            )

            ElevatedCardAddDataScreen(
                title = "Dinner",
                modifier = Modifier.fillMaxWidth()
            )

            FilledCardExample(
                title = "",
                modifier = Modifier.fillMaxWidth()
            )

            ElevatedCardAddDataScreen(
                title = "Snack",
                modifier = Modifier.fillMaxWidth()
            )

            FilledCardExample(
                title = "",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

        }


    }

}

@Preview(showBackground = true)
@Composable
fun MealTimePreview() {
    MealTimeSelection()
}

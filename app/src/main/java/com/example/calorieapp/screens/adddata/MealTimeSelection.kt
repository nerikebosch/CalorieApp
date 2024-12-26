package com.example.calorieapp.screens.adddata

import androidx.appcompat.app.AppCompatActivity // For MaterialDatePicker usage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding // For padding modifiers
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon // For icons in navigation buttons
import androidx.compose.material3.IconButton // For clickable navigation icons
import androidx.compose.material3.MaterialTheme // For styling text and colors
import androidx.compose.material3.Surface // For the Surface container
import androidx.compose.material3.Text // For displaying selected date as text
import androidx.compose.material3.TextButton // For clickable date text button
import androidx.compose.material3.TopAppBar // For creating the top app bar
import androidx.compose.material3.TopAppBarDefaults // For default styling of the top app bar
import androidx.compose.runtime.Composable // For defining composable functions
import androidx.compose.runtime.mutableStateOf // For managing state in Compose
import androidx.compose.runtime.remember // For remembering state across recompositions
import androidx.compose.ui.Modifier // For modifying UI elements
import androidx.compose.ui.platform.LocalContext // For accessing the local context
import androidx.compose.ui.res.painterResource // For loading drawable resources
import androidx.compose.ui.tooling.preview.Preview // For the preview annotation
import androidx.compose.ui.unit.dp // For specifying padding and size
import com.example.calorieapp.R // Replace with your actual package for resources (arrows)
import com.example.calorieapp.screens.homescreen.TabRowExample
import com.google.android.material.datepicker.MaterialDatePicker // For the Material Date Picker
import java.text.SimpleDateFormat // For formatting dates
import java.util.Calendar // For working with dates
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealTimeSelection(){
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
    val calendar = remember { mutableStateOf(Calendar.getInstance()) }

    val selectedDate = remember { mutableStateOf(dateFormatter.format(calendar.value.time)) }

    Surface() {
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
                            modifier = Modifier.align(androidx.compose.ui.Alignment.CenterVertically)
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

            TabRowExample()
        }


    }

}

@Preview(showBackground = true)
@Composable
fun MealTimePreview() {
    MealTimeSelection()
}

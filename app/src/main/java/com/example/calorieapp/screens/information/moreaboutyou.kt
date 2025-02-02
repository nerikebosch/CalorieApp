package com.example.calorieapp.screens.information

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Locale


/**
 * Composable function representing the "More About You" screen.
 * This screen allows users to input additional personal information such as weight, height, gender, date of birth, and water intake goal.
 *
 * @param openScreen Function to handle navigation.
 * @param viewModel ViewModel handling user input and data persistence.
 */
@Composable
fun MoreAboutYouScreen(
    openScreen: (String) -> Unit,
    viewModel: MoreAboutYouViewModel = hiltViewModel()
) {
    val goalWeight by viewModel.user.map { it.goalWeight.toString() }.collectAsState("")
    val weight by viewModel.user.map { it.weight.toString() }.collectAsState("")
    val height by viewModel.user.map { it.height.toString() }.collectAsState("")
    val gender by viewModel.user.map { it.gender }.collectAsState("")
    val dob by viewModel.user.map { it.dob }.collectAsState("")
    val age by viewModel.user.map { it.age.toString() }.collectAsState("")
    val goalWater by viewModel.user.map { it.goalWater.toString() }.collectAsState("")

    MoreAboutYouContent(
        goalWeight = goalWeight,
        weight = weight,
        height = height,
        gender = gender,
        dob = dob,
        age = age,
        goalWater = goalWater,
        onContinueClick = { viewModel.onContinueClick(openScreen) },
        onGoalWeightChange = viewModel::onGoalWeightChange,
        onWeightChange = viewModel::onWeightChange,
        onHeightChange = viewModel::onHeightChange,
        onGenderChange = viewModel::onGenderChange,
        onDobChange = viewModel::onDobChange,
        onAgeChange = viewModel::onAgeChange,
        onGoalWaterChange = viewModel::onGoalWaterChange
    )
}


/**
 * Composable function that renders the UI content for the "More About You" screen.
 * Includes input fields for user details such as weight, height, gender, and date of birth.
 *
 * @param goalWeight The user's goal weight as a string.
 * @param weight The user's current weight as a string.
 * @param height The user's height as a string.
 * @param gender The user's selected gender.
 * @param dob The user's date of birth.
 * @param age The calculated age of the user.
 * @param goalWater The user's daily water intake goal.
 * @param onContinueClick Callback triggered when the continue button is clicked.
 * @param onGoalWeightChange Callback for updating goal weight.
 * @param onWeightChange Callback for updating weight.
 * @param onHeightChange Callback for updating height.
 * @param onGenderChange Callback for updating gender selection.
 * @param onDobChange Callback for updating date of birth.
 * @param onAgeChange Callback for updating age.
 * @param onGoalWaterChange Callback for updating water intake goal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreAboutYouContent(
    goalWeight: String,
    weight: String,
    height: String,
    gender: String,
    dob: String,
    age: String,
    goalWater: String,
    onContinueClick: () -> Unit = {},
    onGoalWeightChange: (Double) -> Unit = {},
    onWeightChange: (Double) -> Unit = {},
    onHeightChange: (Double) -> Unit = {},
    onGenderChange: (String) -> Unit = {},
    onDobChange: (String) -> Unit = {},
    onAgeChange: (Int) -> Unit = {},
    onGoalWaterChange: (Double) -> Unit = {},

) {
    var weightText by remember { mutableStateOf(weight) }
    var goalWaterText by remember { mutableStateOf("") }
    var goalWeightText by remember { mutableStateOf(goalWeight) }
    var heightText by remember { mutableStateOf(height) }
    var selectedDate by remember { mutableStateOf("") } // Store the selected date as a string
    var ageText by remember { mutableStateOf("") }
    var genderText by remember { mutableStateOf("") }
    var genderExpanded by remember { mutableStateOf(false) }
    val genderList = listOf("Male", "Female")
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf(dob) }

    val datePickerState = rememberDatePickerState()

    // Context for the date picker
    val context = LocalContext.current

    Surface {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "A little more about you",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Tell us about yourself",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Gender Selection using ExposedDropdownMenuBox
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { genderText = it },
                    label = { Text("Gender") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = if (genderExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            Modifier.clickable { genderExpanded = !genderExpanded }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(), // Ensures dropdown appears directly below the text field
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderList.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(text = label) },
                            onClick = {
                                onGenderChange(label)
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            InformationTextField(
                value = weightText,
                onValueChange = {
                    weightText = it
                    onWeightChange(it.toDoubleOrNull() ?: 0.0) // Update ViewModel
                },
                label = "Weight",
                unit = "kg",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(25.dp))

            InformationTextField(
                value = goalWeightText,
                onValueChange = {
                    goalWeightText = it
                    onGoalWeightChange(it.toDoubleOrNull() ?: 0.0) // Update ViewModel
                },
                label = "Goal Weight",
                unit = "kg",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(25.dp))

            InformationTextField(
                value = heightText,
                onValueChange = {
                    heightText = it
                    onHeightChange(it.toDoubleOrNull() ?: 0.0)},
                label = "Height",
                unit = "cm",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(25.dp))

            InformationTextField(
                value = goalWaterText,
                onValueChange = {
                    goalWaterText = it
                    onGoalWaterChange(it.toDoubleOrNull() ?: 0.0)},
                label = "Goal Water",
                unit = "ml",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Date of Birth Picker (Instead of Age Input)
            OutlinedTextField(
                value = selectedDateText,
                onValueChange = {},
                label = { Text("Date of Birth") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                }
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                            val selectedMillis = datePickerState.selectedDateMillis
                            if (selectedMillis != null) {
                                val formattedDate = convertMillisToDate(selectedMillis)
                                val calculatedAge = calculateAge(selectedMillis)
                                selectedDateText = formattedDate
                                onDobChange(formattedDate)
                                onAgeChange(calculatedAge)
                            }
                        }) {
                            Text("OK")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = { onContinueClick()},
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

/**
 * Converts a given timestamp (milliseconds) to a formatted date string (DD/MM/YYYY).
 *
 * @param millis The timestamp in milliseconds.
 * @return The formatted date as a string.
 */
fun convertMillisToDate(millis: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
    return String.format(
        Locale.getDefault(), "%02d/%02d/%04d",
        calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.YEAR)
    )
}

/**
 * Calculates the age based on the given timestamp (milliseconds).
 *
 * @param millis The timestamp representing the date of birth.
 * @return The calculated age as an integer.
 */
fun calculateAge(millis: Long): Int {
    val dobCalendar = Calendar.getInstance().apply { timeInMillis = millis }
    val today = Calendar.getInstance()

    var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
        age -= 1
    }
    return age
}


/**
 * Displays a date picker dialog, allowing users to select their date of birth.
 * Once a date is selected, the age is calculated and passed to the provided callback.
 *
 * @param context The application context for displaying the dialog.
 * @param onDateSelected Callback function that receives the selected date and calculated age.
 */
fun showDatePicker(context: android.content.Context, onDateSelected: (String, Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
        val dob = Calendar.getInstance()
        dob.set(selectedYear, selectedMonth, selectedDay)

        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age -= 1 // Adjust for birthday not yet reached
        }

        val formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)

        // Pass selected date and calculated age to ViewModel
        onDateSelected(formattedDate, age)
    }, year, month, day).show()
}

/**
 * Preview function for the "More About You" screen.
 * Displays a sample UI for testing and design purposes.
 */
@Preview(showBackground = true)
@Composable
fun MoreAboutYouPreview() {
    MoreAboutYouContent(
        goalWeight = "60",
        onContinueClick = {},
        onGoalWeightChange = {},
        onGoalWaterChange = {},
        onWeightChange = {},
        weight = "70",
        height = "180",
        onHeightChange = {},
        gender = "Male",
        onGenderChange = {},
        dob = "01/01/2000",
        onDobChange = {},
        age = "23",
        onAgeChange = {},
        goalWater = "2000"
    )
}

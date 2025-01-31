package com.example.calorieapp.screens.statistics

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


/**
 * Composable function representing the statistics screen.
 *
 * @param openScreen A lambda function to navigate to a different screen.
 * @param viewModel The ViewModel for managing UI state and data fetching.
 */
@Composable
fun StatsScreen(
    openScreen: (String) -> Unit,
    viewModel: StatsScreenViewModel = hiltViewModel()
){
    val uiState = viewModel.uiState.collectAsState().value

    StatsScreenContent(
        uiState = uiState,
        viewModel = viewModel
    )
}


/**
 * Displays the content of the statistics screen, including date selection and a weekly calorie chart.
 *
 * @param uiState The current UI state containing calorie data and goals.
 * @param viewModel The ViewModel responsible for fetching and managing statistics data.
 */
@Composable
fun StatsScreenContent(
    uiState: StatsScreenUiState,
    viewModel: StatsScreenViewModel
) {
    val calendar = remember { Calendar.getInstance() }
    val dateFormatter = remember { SimpleDateFormat("MMM d", Locale.getDefault()) }
    val (selectedDate, setSelectedDate) = remember { mutableStateOf(calendar.timeInMillis) }

    // Observe the week range for the selected date
    val getWeekRange: (Long) -> Pair<Date, Date> = { dateMillis ->
        val calendar = Calendar.getInstance().apply { timeInMillis = dateMillis }

        // Ensure correct week selection
        calendar.firstDayOfWeek = Calendar.MONDAY

        val startOfWeek = (calendar.clone() as Calendar).apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }.time

        val endOfWeek = (calendar.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, 2) // Move forward 6 days to get Sunday
        }.time

        startOfWeek to endOfWeek
    }

    val weekRange = remember(selectedDate) { getWeekRange(selectedDate) }
    val weekRangeText = "${dateFormatter.format(weekRange.first)} - ${dateFormatter.format(weekRange.second)}"

    // Fetch new data when the selected date changes
    LaunchedEffect(selectedDate) {
        viewModel.fetchWeeklyCaloriesForWeek(selectedDate)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Week Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Previous Week Button
            TextButton(onClick = {
                setSelectedDate(selectedDate - 7 * 24 * 60 * 60 * 1000) // Move back 7 days
            }) {
                Text(text = "< Previous")
            }

            // Week Range Display
            Text(
                text = weekRangeText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Next Week Button
            TextButton(onClick = {
                setSelectedDate(selectedDate + 7 * 24 * 60 * 60 * 1000) // Move forward 7 days
            }) {
                Text(text = "Next >")
            }
        }

        // Weekly Calories Bar Chart
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Weekly Calories",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Display calorie data for the week
                TextWithBarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    data = uiState.listCalories, // Use fetched weekly data
                    labels = listOf("M", "T", "W", "T", "F", "S", "S"),
                    maxValue = uiState.goalCalorie * 1.5,
                    goalValue = uiState.goalCalorie,
                    chartHeight = 200.dp
                )
            }
        }
    }
}





/**
 * Displays a bar chart representing calorie intake for a week.
 *
 * @param modifier Modifier to adjust layout properties.
 * @param data A list of calorie intake values.
 * @param labels Labels for each day of the week.
 * @param maxValue The maximum calorie intake used for scaling the bars.
 * @param goalValue The goal calorie intake, indicated by a horizontal line.
 * @param barColor Color of the bars.
 * @param goalLineColor Color of the goal line.
 * @param axisColor Color of the axis.
 * @param labelTextSize Font size of labels.
 * @param labelSpacing Space between labels and the chart.
 * @param barSpacing Space between bars.
 * @param chartHeight The height of the chart.
 */
@Composable
fun TextWithBarChart(
    modifier: Modifier = Modifier,
    data: List<Double>, // Changed to Double
    labels: List<String>,
    maxValue: Double, // Changed to Double
    goalValue: Double, // Changed to Double
    barColor: Color = Color(0xFF4CAF50),
    goalLineColor: Color = Color.Black,
    axisColor: Color = Color.Gray,
    labelTextSize: TextUnit = 13.sp,
    labelSpacing: Dp = 10.dp,
    barSpacing: Dp = 10.dp,
    chartHeight: Dp = 300.dp // Parameter to control chart height
) {
    //require(data.size == labels.size) { "Data and labels must have the same size" }

    val density = LocalDensity.current
    val barSpacingPx = with(density) { barSpacing.toPx() }
    val canvasWidthPx = with(density) { 314.dp.toPx() } // Assume the chart width matches the Canvas width
    val barWidth = (canvasWidthPx - (data.size - 1) * barSpacingPx) / data.size // Calculate bar width here

    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        // Draw the bar chart with the specified height
        Canvas(
            modifier = Modifier
                .width(400.dp)
                .height(chartHeight) // Adjust height dynamically
        ) {
            val canvasHeight = size.height

            // Draw horizontal axis (X-axis)
            drawLine(
                color = axisColor,
                start = androidx.compose.ui.geometry.Offset(0f, canvasHeight),
                end = androidx.compose.ui.geometry.Offset(size.width, canvasHeight),
                strokeWidth = 2f
            )

            // Draw bars
            data.forEachIndexed { index, value ->
                val barHeight = (value / maxValue) * canvasHeight
                val xPosition = index * (barWidth + barSpacingPx)
                val yPosition = canvasHeight - barHeight.toFloat()

                // Draw the bar
                drawRect(
                    color = barColor,
                    topLeft = androidx.compose.ui.geometry.Offset(xPosition+35, yPosition),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight.toFloat())
                )
            }

            // Draw the goal line
            val goalLineY = canvasHeight - (goalValue / maxValue * canvasHeight).toFloat()
            drawLine(
                color = goalLineColor,
                start = androidx.compose.ui.geometry.Offset(0f, goalLineY),
                end = androidx.compose.ui.geometry.Offset(size.width, goalLineY),
                strokeWidth = 3f
            )

            // Draw the "Goal" text
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    "Goal",
                    size.width - 50.dp.toPx(),
                    goalLineY - 5.dp.toPx(),
                    TextPaint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 14.sp.toPx()
                        isAntiAlias = true
                    }
                )
            }
        }

        // Draw labels below the bar chart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = labelSpacing),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            labels.forEachIndexed { index, label ->
                val barCenterX = 5 // Center of each bar
                BasicText(
                    text = label,
                    style = TextStyle(color = Color.Black, fontSize = labelTextSize),
                    modifier = Modifier
                        .offset(x = with(density) { barCenterX.toDp() - labelTextSize.toDp() / 20 }) // Center the label
                )
            }
        }
    }
}

package com.example.calorieapp.screens.statistics

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.roundToInt

@Composable
fun StatsScreen(
    openScreen: (String) -> Unit,
    viewModel: StatsScreenViewModel = hiltViewModel()
){
    StatsScreenContent()
}

@Composable
fun StatsScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Bar Chart inside an ElevatedCard
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Add padding around the card
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp) // Add padding inside the card
                    .fillMaxWidth()
            ) {
                // Add a title for the chart (optional)
                Text(
                    text = "Weekly Calories",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // The bar chart
                TextWithBarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp), // Adjust height here
                    data = listOf(2000.0, 1500.0, 1800.0, 1700.0, 2000.0, 1400.0, 1600.0), // Use Double values
                    labels = listOf("M", "T", "W", "T", "F", "S", "S"),
                    maxValue = 2400.0, // Use Double value
                    goalValue = 1800.0, // Use Double value
                    chartHeight = 200.dp // Control the chart height dynamically
                )
            }
        }
    }
}




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
    require(data.size == labels.size) { "Data and labels must have the same size" }

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
                    topLeft = androidx.compose.ui.geometry.Offset(xPosition, yPosition),
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
                        .offset(x = with(density) { barCenterX.toDp() - labelTextSize.toDp() / 5 }) // Center the label
                )
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    StatsScreenContent()
}

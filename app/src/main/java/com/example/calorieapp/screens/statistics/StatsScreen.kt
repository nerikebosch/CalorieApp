//package com.example.calorieapp.screens.statistics
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import co.yml.charts.axis.AxisData
//import co.yml.charts.common.model.DataPoint
//import co.yml.charts.ui.linechart.LineChart
//import co.yml.charts.ui.linechart.model.GridLines
//import co.yml.charts.ui.linechart.model.IntersectionPoint
//import co.yml.charts.ui.linechart.model.Line
//import co.yml.charts.ui.linechart.model.LineChartData
//import co.yml.charts.ui.linechart.model.LinePlotData
//import co.yml.charts.ui.linechart.model.LineStyle
//import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
//import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
//import co.yml.charts.ui.linechart.model.ShadowUnderLine
//
//@Composable
//fun StatsScreen(
//    openScreen: (String) -> Unit,
//    openAndPopUp: (String, String) -> Unit,
//    viewModel: StatsScreenViewModel = hiltViewModel()
//) {
//    StatsScreenContent()
//}
//
//@Composable
//fun StatsScreenContent() {
//    // Define some sample data points
//    val pointsData: List<DataPoint> = listOf(
//        DataPoint(0f, 40f),
//        DataPoint(1f, 90f),
//        DataPoint(2f, 0f),
//        DataPoint(3f, 60f),
//        DataPoint(4f, 10f)
//    )
//
//    // Define the number of steps for the Y-axis
//    val steps = 5
//
//    val xAxisData = AxisData.Builder()
//        .axisStepSize(50.dp)  // Adjusted for better scaling
//        .backgroundColor(Color.Blue)
//        .steps(pointsData.size - 1)
//        .labelData { i -> i.toString() }
//        .labelAndAxisLinePadding(15.dp)
//        .build()
//
//    val yAxisData = AxisData.Builder()
//        .steps(steps)
//        .backgroundColor(Color.Red)
//        .labelAndAxisLinePadding(20.dp)
//        .labelData { i -> (i * (100 / steps)).toString() }
//        .build()
//
//    val lineChartData = LineChartData(
//        linePlotData = LinePlotData(
//            lines = listOf(
//                Line(
//                    dataPoints = pointsData,
//                    lineStyle = LineStyle(),
//                    intersectionPoint = IntersectionPoint(),
//                    selectionHighlightPoint = SelectionHighlightPoint(),
//                    shadowUnderLine = ShadowUnderLine(),
//                    selectionHighlightPopUp = SelectionHighlightPopUp()
//                )
//            )
//        ),
//        xAxisData = xAxisData,
//        yAxisData = yAxisData,
//        gridLines = GridLines(),
//        backgroundColor = Color.White
//    )
//
//    Surface(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            LineChart(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp),
//                lineChartData = lineChartData
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun StatsScreenPreview() {
//    StatsScreenContent()
//}

//package com.example.calorieapp.screens.maps
//
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import ir.ehsannarmani.compose_charts.ColumnChart
//import ir.ehsannarmani.compose_charts.models.BarProperties
//import ir.ehsannarmani.compose_charts.models.Bars
//import ir.ehsannarmani.compose_charts.models.DrawStyle
//
//@Composable
//fun Graph(){
//
//    Surface {
//        ColumnChart(
//            modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp),
//            data = remember {
//                listOf(
//                    Bars(
//                        label = "Jan",
//                        values = listOf(
//                            Bars.Data(label = "Win", value = 70.0, color = SolidColor(Color.Red))
//                        ),
//                    ),
//                    Bars(
//                        label = "Feb",
//                        values = listOf(
//                            Bars.Data(label = "Windows", value = 60.0, color = SolidColor(Color.Blue))
//                        ),
//                    )
//                )
//            },
//            barProperties = BarProperties(
//                thickness = 15.dp,
//                spacing = 4.dp,
//                cornerRadius = Bars.Data.Radius.Circular(6.dp),
//                style = DrawStyle.Fill
//            ),
//            maxValue = 100.0
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GraphPreview(){
//    Graph()
//}
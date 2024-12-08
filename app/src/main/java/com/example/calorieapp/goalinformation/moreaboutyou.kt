package com.example.calorieapp.goalinformation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calorieapp.loginscreens.LoginTextField

@Composable
fun moreAboutYou() {
    val weight = remember { mutableStateOf("") }
    val height = remember { mutableStateOf("") }
    val age = remember { mutableStateOf("") }

    Surface {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)
            , horizontalAlignment = Alignment.CenterHorizontally) {
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

            InformationTextField(
                value = weight.value,
                onValueChange = { weight.value = it },
                label = "Weight",
                unit = "kg",
                modifier = Modifier.fillMaxWidth(),

            )
            Spacer(modifier = Modifier.height(25.dp))
            InformationTextField(
                value = height.value,
                onValueChange = { height.value = it },
                label = "Height",
                unit = "cm",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(25.dp))
            InformationTextField(
                value = age.value,
                onValueChange = { age.value = it },
                label = "Age",
                unit = "",
                modifier = Modifier.fillMaxWidth(),

            )
            Spacer(modifier = Modifier.height(300.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                onClick = { /*TODO*/ },
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


@Preview(showBackground = true)
@Composable
fun MoreAboutYouPreview() {
    moreAboutYou()
}
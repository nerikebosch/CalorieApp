package com.example.calorieapp.screens.information

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun MoreAboutYou() {
    val weight = remember { mutableStateOf("") }
    val goalweight = remember { mutableStateOf("") }
    val height = remember { mutableStateOf("") }
    val age = remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var genderexpanded by remember { mutableStateOf(false) }
    val genderlist = listOf("Male", "Female", "Prefer not to say")
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    // Dropdown for gender icon
    val icon = if (genderexpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

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
            InformationTextFieldGender(
                value = gender,
                onValueChange = { gender = it },
                label = "Gender",
                unit = "",
                trailingIcon = {
                    Icon(icon, contentDescription = null,
                        Modifier.clickable { genderexpanded = !genderexpanded })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    }
            )
            DropdownMenu(
                expanded = genderexpanded,
                onDismissRequest = { genderexpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { if (mTextFieldSize.width > 0) mTextFieldSize.width.toDp() else 50.dp })
            ) {
                genderlist.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(text = label) }, onClick = {
                            gender = label
                            genderexpanded = false
                        })
                }
            }

            Spacer(modifier = Modifier.height(25.dp))
            InformationTextField(
                value = weight.value,
                onValueChange = { weight.value = it },
                label = "Weight",
                unit = "kg",
                modifier = Modifier.fillMaxWidth(),
                )
            Spacer(modifier = Modifier.height(25.dp))
            InformationTextField(
                value = goalweight.value,
                onValueChange = { goalweight.value = it },
                label = "Goal Weight",
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
            Spacer(modifier = Modifier.height(200.dp))

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
    MoreAboutYou()
}
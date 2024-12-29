package com.example.calorieapp.screens.adddata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calorieapp.R

@Composable
fun ElevatedCardAddDataScreen(
    title: String, // Title of the card
    modifier: Modifier = Modifier, // Custom modifier for layout
    content: @Composable (() -> Unit)? = null // Lambda that can accept other composable (like icons, images)
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .size(width = 300.dp, height = 60.dp) // Adjust size as necessary
    ) {
        // Using Column for stacking items vertically (text + icon/content)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // Padding inside the card
                horizontalArrangement = Arrangement.SpaceBetween

        ) {
            // Title text at the top
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(bottom = 8.dp) // Space between title and content
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = null,
                modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
            )



            // Optional content: Show any composable passed into the content lambda
            content?.invoke() // If content is provided, display it here
        }
    }
}


@Composable
fun FilledCardExample(
    title : String,
    modifier: Modifier = Modifier) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth().height(50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = null,
                modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
            )
        }

        if (expanded) {
               
        }
    }
}
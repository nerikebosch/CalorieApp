package com.example.calorieapp.screens.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.calorieapp.R


@Composable
fun ElevatedCardRecipeScreen(
    title: String, // Title of the card
    modifier: Modifier = Modifier, // Custom modifier for layout
    content: @Composable (() -> Unit)? = null, // Lambda that can accept other composable (like icons, images)
    onClick: () -> Unit = {}, // Lambda to handle click event
    img: String? = null
) {

    val imageUrl = img ?: "https://www.thewindowsclub.com/wp-content/uploads/2021/08/theres-nothing-to-show-here.png"


    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .size(width = 400.dp, height = 200.dp) // Adjust size as necessary
    ) {
        // Using Column for stacking items vertically (text + icon/content)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // Padding inside the card
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically

        ) {
            // Display image using Coil's AsyncImage
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .size(200.dp)
                    .clip(MaterialTheme.shapes.medium), // Optional: Add rounded corners
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f), // Takes up remaining space
                verticalArrangement = Arrangement.Center, // Centers text vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers text horizontally
            ) {
                //Spacer(modifier = Modifier.height(8.dp))
                // Title text at the top
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp) // Space between title and content

                )
            }

            // Optional content: Show any composable passed into the content lambda
            content?.invoke() // If content is provided, display it here
        }
    }
}

@Composable
fun FilterChip(
    modifier: Modifier = Modifier
        .padding(8.dp),
    title: String
) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = { selected = !selected },
        label = {
            Text(title)
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

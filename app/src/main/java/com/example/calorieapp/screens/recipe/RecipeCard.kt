package com.example.calorieapp.screens.recipe


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


/**
 * Composable function to display an elevated card representing a recipe.
 *
 * @param title The title of the recipe.
 * @param modifier Modifier for layout customization.
 * @param content Optional composable content to be displayed within the card.
 * @param onClick Callback function triggered on card click.
 * @param img URL of the recipe image.
 */
@Composable
fun ElevatedCardRecipeScreen(
    title: String, // Title of the card
    modifier: Modifier = Modifier, // Custom modifier for layout
    content: @Composable (() -> Unit)? = null, // Lambda that can accept other composable (like icons, images)
    onClick : () -> Unit = {}, // Lambda to handle click event
    img: String? = null
) {

    val imageUrl = img ?: "https://www.thewindowsclub.com/wp-content/uploads/2021/08/theres-nothing-to-show-here.png"


    ElevatedCard(
        onClick = onClick,
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

/**
 * Composable function to display an elevated card for recipe details.
 *
 * @param title Title of the details section.
 * @param modifier Modifier for layout customization.
 * @param content Optional composable content for the details.
 */
@Composable
fun ElevatedCardRecipeDetails(
    title: String, // Title of the card
    modifier: Modifier = Modifier, // Custom modifier for layout
    content: @Composable (() -> Unit)? = null,
){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .wrapContentHeight() // Adjust size as necessary
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp) // Padding inside the card
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically

        ) {

            Column(
                modifier = Modifier
                    .weight(1f) // Takes up remaining space
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center, // Centers text vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers text horizontally
            ) {
                // Title text at the top
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp) // Space between title and content

                )
                content?.invoke()
            }
        }
    }
}

/**
 * Composable function to display a filter chip.
 *
 * @param title The label of the chip.
 * @param selectedCategory The currently selected category.
 * @param onCategorySelected Callback function when a category is selected.
 */
@Composable
fun FilterChip(
    title: String,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    val isSelected = title == selectedCategory

    FilterChip(
        onClick = {
            onCategorySelected(if (isSelected) null else title)
        },
        label = { Text(title) },
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}


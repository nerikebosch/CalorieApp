package com.example.calorieapp.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    currentUser: FirebaseUser?,
    onSignOutClick: () -> Unit
) {
    val navController = rememberNavController()

    Surface(
        modifier = modifier.fillMaxSize(),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Home")

            currentUser?.let { user ->
//                user.photoUrl?.let {
//                    AsyncImage(
//                        modifier = Modifier.size(140.dp)
//                            .clip(RoundedCornerShape(4.dp)),
//                        model = Im
//                }

                user.displayName?.let { name ->
                    Text(text = name)
                }
                user.email?.let { email ->
                    Text(text = email)
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = { onSignOutClick() }) {
                    Text(
                        text = "Sign out",
                    )
                }
            }
        }
    }
}


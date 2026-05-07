package com.quincy.restaurantfinder.ui.theme.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quincy.restaurantfinder.models.Restaurant
import com.quincy.restaurantfinder.models.RestaurantViewModel

@Composable
fun AdminScreen(viewModel: RestaurantViewModel) {

    val restaurants = viewModel.restaurants.value

    // Form states
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadRestaurants()
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Admin Panel", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // NAME
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Restaurant Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LOCATION
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // DESCRIPTION
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // RATING
        OutlinedTextField(
            value = rating,
            onValueChange = { rating = it },
            label = { Text("Rating") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ADD BUTTON
        Button(
            onClick = {
                val restaurant = Restaurant(
                    name = name,
                    location = location,
                    description = description,
                    rating = rating.toDoubleOrNull() ?: 0.0
                )

                viewModel.addRestaurant(restaurant) {
                    name = ""
                    location = ""
                    description = ""
                    rating = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Restaurant")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Restaurants", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(10.dp))

        // LIST
        LazyColumn {

            items(restaurants) { restaurant ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(restaurant.name, style = MaterialTheme.typography.titleMedium)
                        Text(restaurant.location)
                        Text("Rating: ${restaurant.rating}")
                        Text(restaurant.description)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {

                            Button(
                                onClick = {
                                    viewModel.deleteRestaurant(restaurant.id)
                                }
                            ) {
                                Text("Delete")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Edit button placeholder for next step
                            Button(onClick = { /* edit later */ }) {
                                Text("Edit")
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//private fun Adminprev() {
//AdminScreen(
//    viewModel = RestaurantViewModel()
//)
//}
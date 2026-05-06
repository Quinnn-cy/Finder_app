package com.quincy.restaurantfinder.ui.theme.screens.location

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quincy.restaurantfinder.models.LocationViewModel
import com.quincy.restaurantfinder.ui.theme.screens.home.PlaceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    viewModel: LocationViewModel,
    onNavigateToDetails: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Run once when screen opens
    LaunchedEffect(Unit) {
        viewModel.fetchRestaurants()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Restaurants Near You", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading && state.nearbyRestaurants.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.nearbyRestaurants.isNotEmpty() -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(state.nearbyRestaurants) { place ->
                            PlaceCard(
                                place = place,
                                onClick = { onNavigateToDetails(place.place_id) }
                            )
                        }
                    }
                }

                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }

                !state.isLoading && state.nearbyRestaurants.isEmpty() -> {
                    Text(
                        text = "No restaurants found nearby.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

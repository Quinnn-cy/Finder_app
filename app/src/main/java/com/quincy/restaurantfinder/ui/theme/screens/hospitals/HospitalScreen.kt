package com.quincy.restaurantfinder.ui.theme.screens.hospitals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.quincy.restaurantfinder.models.LocationViewModel
import com.quincy.restaurantfinder.ui.theme.RestaurantFinderTheme
import com.quincy.restaurantfinder.ui.components.PlaceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HospitalScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    viewModel: LocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchHospitals()
    }
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    // Show error if it exists
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Column {
                    MediumTopAppBar(
                        title = {
                            Column {
                                Text("Nearby Hospitals", fontWeight = FontWeight.ExtraBold)
                                if (!isSearchActive) {
                                    Text(
                                        text = "${state.nearbyHospitals.size} medical centers found",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        },
                        actions = {
                            IconButton(onClick = { /* TODO: Implement Sort */ }) {
                                Icon(Icons.Default.Tune, contentDescription = "Sort")
                            }
                            IconButton(onClick = { /* TODO: Implement Filter */ }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Filter")
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent
                        )
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    ) {
                        if (isSearchActive) {
                            SearchBar(
                                query = searchQuery,
                                onQueryChange = {
                                    searchQuery = it
                                    if (it.isEmpty()) viewModel.searchHospitals("")
                                },
                                onSearch = { viewModel.searchHospitals(it) },
                                onClearSearch = {
                                    searchQuery = ""
                                    isSearchActive = false
                                    viewModel.searchHospitals("")
                                }
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = { isSearchActive = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Search Hospitals", style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (state.isLoading && state.nearbyHospitals.isEmpty() && state.searchResults.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 3.dp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Search Results Section
                    if (searchQuery.isNotEmpty() || state.searchResults.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                "Search Results",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        if (state.searchResults.isEmpty() && !state.isLoading) {
                            item(span = { GridItemSpan(2) }) {
                                Text(
                                    "No hospitals found matching \"$searchQuery\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            items(state.searchResults) { hospital ->
                                PlaceCard(
                                    place = hospital,
                                    isHospital = true,
                                    onClick = { onNavigateToDetails(hospital.place_id) }
                                )
                            }
                        }

                        item(span = { GridItemSpan(2) }) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        }
                    }

                    // Map Section
                    if (state.latitude != null && state.longitude != null && searchQuery.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            val userLocation = remember(state.latitude, state.longitude) {
                                LatLng(state.latitude!!, state.longitude!!)
                            }
                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(userLocation, 14f)
                            }

                            LaunchedEffect(userLocation) {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(userLocation, 14f)
                                )
                            }

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Map,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            "Interactive Map",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        "Tap markers for details",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                    border = CardDefaults.outlinedCardBorder()
                                ) {
                                    GoogleMap(
                                        modifier = Modifier.fillMaxSize(),
                                        cameraPositionState = cameraPositionState,
                                        properties = MapProperties(isMyLocationEnabled = true),
                                        uiSettings = com.google.maps.android.compose.MapUiSettings(
                                            zoomControlsEnabled = false,
                                            myLocationButtonEnabled = true
                                        )
                                    ) {
                                        state.nearbyHospitals.forEach { hospital ->
                                            Marker(
                                                state = MarkerState(
                                                    position = LatLng(
                                                        hospital.geometry.location.lat,
                                                        hospital.geometry.location.lng
                                                    )
                                                ),
                                                title = hospital.name,
                                                snippet = hospital.vicinity,
                                                onClick = {
                                                    onNavigateToDetails(hospital.place_id)
                                                    true
                                                }
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    "Recommended Nearby",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }
                    }

                    if (state.nearbyHospitals.isEmpty() && !state.isLoading && searchQuery.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(top = 64.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "🏥",
                                    style = MaterialTheme.typography.displayLarge
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    "No hospitals found in this area.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(24.dp))
                                Button(
                                    onClick = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Expanding search to 20km...")
                                        }
                                        viewModel.fetchHospitals(radius = 20000)
                                    },
                                    enabled = !state.isLoading,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    if (state.isLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                        Spacer(Modifier.width(8.dp))
                                    }
                                    Text("Expand search to 20km")
                                }
                            }
                        }
                    } else if (searchQuery.isEmpty()) {
                        items(state.nearbyHospitals) { hospital ->
                            PlaceCard(
                                place = hospital,
                                isHospital = true,
                                onClick = { onNavigateToDetails(hospital.place_id) }
                            )
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }
            }

            if (state.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search for hospitals, clinics...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        trailingIcon = {
            IconButton(onClick = onClearSearch) {
                Icon(Icons.Default.Close, contentDescription = "Clear Search")
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search
        ),
        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
            onSearch = { onSearch(query) }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun Hospitalprev() {
    RestaurantFinderTheme {
        HospitalScreen(
            onNavigateBack = {},
            onNavigateToDetails = {}
        )
    }
}

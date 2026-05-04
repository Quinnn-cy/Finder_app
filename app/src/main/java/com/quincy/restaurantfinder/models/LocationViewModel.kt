package com.quincy.restaurantfinder.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quincy.restaurantfinder.data.model.Place
import com.quincy.restaurantfinder.data.repository.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationState(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val nearbyRestaurants: List<Place> = emptyList(),
    val searchResults: List<Place> = emptyList(),
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val error: String? = null
)

class LocationViewModel : ViewModel() {

    private val placesRepository = PlacesRepository()

    private val _state = MutableStateFlow(LocationState())
    val state= _state.asStateFlow()

    // 1. Set location FIRST
    fun updateLocation(lat: Double, lng: Double) {
        _state.value = _state.value.copy(
            latitude = lat,
            longitude = lng
        )

        fetchRestaurants()
    }

    // 2. Fetch restaurants AFTER location is set
    fun fetchRestaurants() {
        val lat = _state.value.latitude
        val lng = _state.value.longitude

        if (lat == null || lng == null) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                val restaurants = placesRepository.getNearbyRestaurants(lat, lng)

                _state.value = _state.value.copy(
                    nearbyRestaurants = restaurants,
                    isLoading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    suspend fun getPlaceDetails(placeId: String): com.quincy.restaurantfinder.data.model.Place? {
        return placesRepository.getPlaceDetails(placeId)
    }

    fun searchRestaurants(query: String) {
        if (query.isBlank()) {
            _state.value = _state.value.copy(isSearching = false, searchResults = emptyList())
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, isSearching = true)
            try {
                val results = placesRepository.searchRestaurants(query)
                _state.value = _state.value.copy(
                    searchResults = results,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
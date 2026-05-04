package com.quincy.restaurantfinder.data.repository

import com.quincy.restaurantfinder.data.model.Place
import com.quincy.restaurantfinder.data.remote.RetrofitInstance

class PlacesRepository {

    private val apiKey = "AIzaSyAKwniuRGtvdnIBsOI5NnaToJ6wmFtwc6o"

    suspend fun getNearbyRestaurants(
        lat: Double,
        lng: Double
    ): List<Place> {

        val response = RetrofitInstance.api.getNearbyRestaurants(
            location = "$lat,$lng",
            apiKey = apiKey
        )

        return response.results
    }

    suspend fun getPlaceDetails(placeId: String): com.quincy.restaurantfinder.data.model.Place? {
        return try {
            val response = RetrofitInstance.api.getPlaceDetails(placeId, apiKey)
            response.result
        } catch (e: Exception) {
            null
        }
    }

    suspend fun searchRestaurants(query: String): List<com.quincy.restaurantfinder.data.model.Place> {
        return try {
            val response = RetrofitInstance.api.searchRestaurants(query, apiKey = apiKey)
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }
}
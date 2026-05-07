package com.quincy.restaurantfinder.data.repository

import com.quincy.restaurantfinder.BuildConfig
import com.quincy.restaurantfinder.data.model.Place
import com.quincy.restaurantfinder.data.remote.RetrofitInstance

class PlacesRepository {

    private val apiKey = BuildConfig.MAPS_API_KEY

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
    suspend fun getNearbyHospitals(
        lat: Double,
        lng: Double,
        radius: Int = 5000
    ): List<Place> {
        val response = RetrofitInstance.api.getNearbyHospitals(
            location = "$lat,$lng",
            radius = radius,
            apiKey = apiKey
        )
        
        if (response.status != "OK" && response.status != "ZERO_RESULTS") {
            throw Exception(response.error_message ?: "API Error: ${response.status}")
        }

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
    } suspend fun searchHospitals(query: String): List<com.quincy.restaurantfinder.data.model.Place> {
        return try {
            val response = RetrofitInstance.api.searchHospitals(query, apiKey = apiKey)
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }


}
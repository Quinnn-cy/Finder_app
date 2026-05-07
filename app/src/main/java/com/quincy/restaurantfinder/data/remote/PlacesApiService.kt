package com.quincy.restaurantfinder.data.remote

import com.quincy.restaurantfinder.data.model.PlaceDetailsResponse
import com.quincy.restaurantfinder.data.model.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {

    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyRestaurants(
        @Query("location") location: String,
        @Query("radius") radius: Int = 1500,
        @Query("type") type: String = "restaurant",
        @Query("key") apiKey: String
    ): PlacesResponse

    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyHospitals(
        @Query("location") location: String,
        @Query("radius") radius: Int = 5000,
        @Query("type") type: String = "hospital",
        @Query("key") apiKey: String): PlacesResponse

    @GET("maps/api/place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String
    ): PlaceDetailsResponse


    @GET("maps/api/place/textsearch/json")
    suspend fun searchHospitals(
        @Query("query") query: String,
        @Query("type") type: String = "hospital",
        @Query("key") apiKey: String
    ): PlacesResponse

    @GET("maps/api/place/textsearch/json")
    suspend fun searchRestaurants(
        @Query("query") query: String,
        @Query("type") type: String = "restaurant",
        @Query("key") apiKey: String
    ): PlacesResponse

    @GET("maps/api/place/textsearch/json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("type") type: String ="hospital",
        @Query("key") apiKey: String
    ): PlacesResponse
}





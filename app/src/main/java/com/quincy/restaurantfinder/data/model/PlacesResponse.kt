package com.quincy.restaurantfinder.data.model

data class PlacesResponse(
    val results: List<Place>,
    val status: String,
    val error_message: String? = null
)

data class Place(
    val place_id: String,
    val name: String,
    val rating: Double?,
    val user_ratings_total: Int? = null,
    val vicinity: String? = null,
    val formatted_address: String? = null,
    val formatted_phone_number: String? = null,
    val website: String? = null,
    val geometry: Geometry,
    val photos: List<Photo>? = null,
    val opening_hours: OpeningHours? = null
)

data class OpeningHours(
    val open_now: Boolean?,
    val weekday_text: List<String>? = null
)

data class Photo(
    val photo_reference: String
)

data class PlaceDetailsResponse(
    val result: Place
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
package com.quincy.restaurantfinder.models

import com.quincy.restaurantfinder.R

data class Restaurant(
    val id: Int,
    val name: String,
    val rating: Double,
    val location: String,
    val imageRes: Int,
    val latitude: Double,
    val longitude: Double,
    val description: String = "A wonderful place to enjoy a delicious meal with friends and family."
)

val sampleRestaurants = listOf(
    Restaurant(1, "The Golden Fork", 4.5, "Downtown, New York", R.drawable.img, 40.7128, -74.0060, "Exquisite fine dining in the heart of the city."),
    Restaurant(2, "Ocean Breeze", 4.2, "Miami Beach, Florida", R.drawable.img, 25.7617, -80.1918, "Fresh seafood with a stunning ocean view."),
    Restaurant(3, "Mountain Peak Grill", 4.8, "Aspen, Colorado", R.drawable.img, 39.1911, -106.8175, "Cozy grill offering the best steaks in the mountains."),
    Restaurant(4, "Urban Spice", 4.0, "Chicago, Illinois", R.drawable.img, 41.8781, -87.6298, "Authentic spices and flavors from around the world."),
    Restaurant(5, "Rustic Table", 4.6, "Austin, Texas", R.drawable.img, 30.2672, -97.7431, "Farm-to-table experience with a rustic charm.")
)

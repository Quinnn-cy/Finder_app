package com.quincy.restaurantfinder.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.quincy.restaurantfinder.models.Restaurant

class RestaurantRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getRestaurants(
        onSuccess: (List<Restaurant>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        db.collection("restaurants")
            .get()
            .addOnSuccessListener { result ->

                val list = result.map { document ->

                    Restaurant(
                        name = document.getString("name") ?: "",
                        location = document.getString("location") ?: "",
                        rating = document.getDouble("rating") ?: 0.0,
                        id = document.id
                    )
                }

                onSuccess(list)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
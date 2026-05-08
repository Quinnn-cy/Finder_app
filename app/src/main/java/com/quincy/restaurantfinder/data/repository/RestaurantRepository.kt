package com.quincy.restaurantfinder.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.quincy.restaurantfinder.models.Restaurant

class RestaurantRepository {

    private val db = FirebaseFirestore.getInstance()
    private val restaurantCollection = db.collection("restaurants")


    fun getRestaurants(
        onSuccess: (List<Restaurant>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        db.collection("restaurants")
            .get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { document ->
                    document.toObject(Restaurant::class.java)?.copy(id = document.id)
                }
                onSuccess(list)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
    // ADD RESTAURANT
    fun addRestaurant(
        restaurant: Restaurant,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val document = restaurantCollection.document()

        val newRestaurant = restaurant.copy(id = document.id)

        document.set(newRestaurant)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }



    // DELETE RESTAURANT
    fun deleteRestaurant(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        restaurantCollection.document(id)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    // UPDATE RESTAURANT
    fun updateRestaurant(
        restaurant: Restaurant,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        restaurantCollection.document(restaurant.id)
            .set(restaurant)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }
}










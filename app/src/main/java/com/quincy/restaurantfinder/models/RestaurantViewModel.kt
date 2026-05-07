package com.quincy.restaurantfinder.models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.quincy.restaurantfinder.data.repository.RestaurantRepository

class RestaurantViewModel : ViewModel() {

    private val repository = RestaurantRepository()

    private val _restaurants = mutableStateOf<List<Restaurant>>(emptyList())
    val restaurants: State<List<Restaurant>> = _restaurants

    // LOAD RESTAURANTS
    fun loadRestaurants() {

        repository.getRestaurants(
            onSuccess = {
                _restaurants.value = it
            },
            onFailure = {
                it.printStackTrace()
            }
        )
    }

    // ADD RESTAURANT
    fun addRestaurant(
        restaurant: Restaurant,
        onComplete: () -> Unit
    ) {

        repository.addRestaurant(
            restaurant = restaurant,
            onSuccess = {
                loadRestaurants()
                onComplete()
            },
            onFailure = {
                it.printStackTrace()
            }
        )
    }

    // DELETE RESTAURANT
    fun deleteRestaurant(id: String) {

        repository.deleteRestaurant(
            id = id,
            onSuccess = {
                loadRestaurants()
            },
            onFailure = {
                it.printStackTrace()
            }
        )
    }

    // UPDATE RESTAURANT
    fun updateRestaurant(
        restaurant: Restaurant
    ) {

        repository.updateRestaurant(
            restaurant = restaurant,
            onSuccess = {
                loadRestaurants()
            },
            onFailure = {
                it.printStackTrace()
            }
        )
    }
}
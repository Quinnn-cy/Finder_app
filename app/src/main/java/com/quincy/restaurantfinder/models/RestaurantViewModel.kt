package com.quincy.restaurantfinder.models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.quincy.restaurantfinder.data.repository.RestaurantRepository

class RestaurantViewModel : ViewModel() {

    private val repository = RestaurantRepository()

    private val _restaurants = mutableStateOf<List<Restaurant>>(emptyList())
    val restaurants: State<List<Restaurant>> = _restaurants

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
}

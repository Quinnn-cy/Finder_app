package com.quincy.restaurantfinder.models

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quincy.restaurantfinder.data.model.Place
import com.quincy.restaurantfinder.data.remote.RetrofitInstance
import com.quincy.restaurantfinder.data.repository.RestaurantRepository
import kotlinx.coroutines.launch
import java.util.Calendar.AM

class RestaurantViewModel : ViewModel() {

    private val repository = RestaurantRepository()

    private val _restaurants = mutableStateOf<List<Restaurant>>(emptyList())
    val hospitals = mutableStateOf<List<Place>>(emptyList())
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
//    LOAD HOSPITALS
fun loadHospitals(latitude: Double, longitude: Double) {

    viewModelScope.launch {

        try {

            val response = RetrofitInstance.api.getNearbyHospitals(
                location = "$latitude,$longitude",
                radius = 20000,
                type = "hospital",
                apiKey = "AIzaSyAwzW2WO71Na1qbXhgAtzoyVHnkwBlT-AM"
            )

            hospitals.value = response.results

        } catch (e: Exception) {
            Log.e("HOSPITAL_ERROR", e.message.toString())
        }
    }
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
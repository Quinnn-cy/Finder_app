package com.quincy.restaurantfinder

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.firestore.FirebaseFirestore
import com.quincy.restaurantfinder.navigation.AppNavHost
import com.quincy.restaurantfinder.ui.theme.RestaurantFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantFinderTheme {
                AppNavHost()
            }
        }



        val db = FirebaseFirestore.getInstance()

        val restaurant = hashMapOf(
            "name" to "Test Restaurant",
            "location" to "Nairobi",
            "rating" to 4.5
        )

        db.collection("restaurants")
            .add(restaurant)
            .addOnSuccessListener {
                Log.d("FIREBASE", "Data added successfully")
            }
            .addOnFailureListener {
                Log.d("FIREBASE", "Error adding data")
            }
    }
}

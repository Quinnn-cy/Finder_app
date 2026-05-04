package com.quincy.restaurantfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
    }
}

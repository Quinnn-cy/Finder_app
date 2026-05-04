package com.quincy.restaurantfinder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quincy.restaurantfinder.ui.theme.screens.details.DetailsScreen
import com.quincy.restaurantfinder.ui.theme.screens.home.HomeScreen
import com.quincy.restaurantfinder.ui.theme.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(onTimeout = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.HOME) {
            HomeScreen(onNavigateToDetails = { placeId ->
                navController.navigate("details/$placeId")
            })
        }
        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("placeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString("placeId") ?: ""
            DetailsScreen(
                placeId = placeId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

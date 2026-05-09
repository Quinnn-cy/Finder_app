package com.quincy.restaurantfinder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quincy.restaurantfinder.models.RestaurantViewModel
import com.quincy.restaurantfinder.ui.theme.screens.admin.AdminRestaurantsScreen
import com.quincy.restaurantfinder.ui.theme.screens.admin.AdminScreen
import com.quincy.restaurantfinder.ui.theme.screens.details.DetailsScreen
import com.quincy.restaurantfinder.ui.theme.screens.home.HomeScreen
import com.quincy.restaurantfinder.ui.theme.screens.hospitals.HospitalScreen
import com.quincy.restaurantfinder.ui.theme.screens.login.LoginScreen
import com.quincy.restaurantfinder.ui.theme.screens.register.RegisterScreen
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
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = if (role == "admin") Routes.ADMIN else Routes.HOME
                    navController.navigate(destination) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.ADMIN) {
            val restaurantViewModel: RestaurantViewModel = viewModel()
            AdminScreen(viewModel = restaurantViewModel)
        }
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToDetails = { placeId ->
                    navController.navigate("details/$placeId")
                },
                onNavigateToHospitals = {
                    navController.navigate(Routes.HOSPITALS)
                },
                onNavigateToAdminRestaurants = {
                    navController.navigate(Routes.ADMIN_RESTAURANTS)
                }
            )
        }
        composable(Routes.ADMIN_RESTAURANTS) {
            val restaurantViewModel: RestaurantViewModel = viewModel()
            AdminRestaurantsScreen(
                viewModel = restaurantViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.HOSPITALS) {
            HospitalScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetails = { placeId ->
                    navController.navigate("details/$placeId")
                }
            )
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

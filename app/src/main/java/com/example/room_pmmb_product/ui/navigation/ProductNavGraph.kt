package com.example.room_pmmb_product.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.room_pmmb_product.ui.home.HomeViewModel
import com.example.room_pmmb_product.ui.home.ProductDestination
import com.example.room_pmmb_product.ui.home.ProductScreen
import com.example.room_pmmb_product.ui.product.ProductEditDestination
import com.example.room_pmmb_product.ui.product.ProductEditScreen
import com.example.room_pmmb_product.ui.product.ProductDetailsDestination
import com.example.room_pmmb_product.ui.product.ProductDetailsScreen
import com.example.room_pmmb_product.ui.product.ProductEntryScreen
import com.example.room_pmmb_product.ui.product.ProductEntryDestination
/**
 * Provides Navigation graph for the application.
 */
@Composable
fun ProductNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = ProductDestination.route,
        modifier = modifier
    ) {
        composable(route = ProductDestination.route) {
            ProductScreen(
                navigateToProductEntry = { navController.navigate(ProductEntryDestination.route) },
                navigateToProductUpdate = {
                    navController.navigate("${ProductDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ProductEntryDestination.route) {
            ProductEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ProductDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ProductDetailsDestination.productIdArg) {
                type = NavType.IntType
            })
        ) {
            ProductDetailsScreen(
                navigateToEditProduct = { navController.navigate("${ProductEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ProductEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ProductEditDestination.productIdArg) {
                type = NavType.IntType
            })
        ) {
            ProductEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}


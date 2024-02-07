package com.petter.remembeer.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.screens.AllBeerScreen
import com.petter.remembeer.screens.BeerDetailScreen
import com.petter.remembeer.screens.BeerScreen
import com.petter.remembeer.screens.NavigationItem
import com.petter.remembeer.screens.ReceivedBeerScreen
import com.petter.remembeer.screens.SettingsScreen
import java.util.UUID


@Composable
fun Navigations(navController: NavHostController) {

    /*val navController = rememberNavController()
    val viewModel: BeerViewModel = viewModel()*/

    NavHost(navController, startDestination = NavigationItem.MyBeer.route) {
        composable(NavigationItem.MyBeer.route) {
            BeerScreen()
        }
        composable(NavigationItem.ReceivedBeer.route) {
            ReceivedBeerScreen()
        }
        composable(NavigationItem.AllBeer.route) {
            AllBeerScreen()
        }
        composable(NavigationItem.Settings.route) {
            SettingsScreen()
        }
        /*composable(
            route = "${NavigationItem.BeerDetail.route}/{beerId}",
            arguments = listOf(navArgument("beerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val beerId = UUID.fromString(backStackEntry.arguments?.getString("beerId") ?: "")
            val selectedBeer = viewModel.getBeerById(beerId)

            if (selectedBeer != null) {
                BeerDetailScreen(navController, viewModel, selectedBeer)
            } else {
                // Handle case where selected beer is null
                Text("Selected beer not found")
            }
        }*/

        composable(NavigationItem.BeerDetail.route) {
            BeerDetailScreen()
        }
    }
}

package com.petter.remembeer.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.screens.AllBeerScreen
import com.petter.remembeer.screens.BeerDetailScreen
import com.petter.remembeer.screens.BeerScreen
import com.petter.remembeer.screens.BeerTypePictureScreen
import com.petter.remembeer.screens.NavigationItem
import com.petter.remembeer.screens.ReceivedBeerScreen
import com.petter.remembeer.screens.SettingsScreen
import java.util.UUID


@Composable
fun Navigations(navController: NavHostController, viewModel: BeerViewModel) {


    NavHost(navController, startDestination = NavigationItem.MyBeer.route) {
        composable(NavigationItem.MyBeer.route) {
            BeerScreen(navController, viewModel)
        }
        composable(NavigationItem.ReceivedBeer.route) {
            ReceivedBeerScreen(viewModel)
        }
        composable(NavigationItem.AllBeer.route) {
            AllBeerScreen()
        }
        composable(NavigationItem.Settings.route) {
            SettingsScreen()
        }
        /*composable(NavigationItem.ShowCamera.route){
            ShowCameraScreen(viewModel)
        }*/
        composable(NavigationItem.BeerDetail.route + "/{beerId}") { backStackEntry ->
            val beerId = UUID.fromString(backStackEntry.arguments?.getString("beerId") ?: "")
            val selectedBeer = viewModel.getBeerById(beerId)

            if (selectedBeer != null) {
                BeerDetailScreen(navController, viewModel, selectedBeer)
            } else {
                // Handle case where selected beer is null
                Text("Selected beer not found")
            }
        }
        composable(NavigationItem.BeerType.route + "/{beerId}") { backStackEntry ->
            val beerId = UUID.fromString(backStackEntry.arguments?.getString("beerId") ?: "")
            val selectedBeer = viewModel.getBeerById(beerId)

            if (selectedBeer != null) {
                BeerTypePictureScreen(navController,viewModel, selectedBeer)
            } else {
                // Handle case where selected beer is null
                Text("Selected beer not found")
            }
        }
    }
}


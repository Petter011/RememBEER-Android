package com.petter.remembeer.ui.navigation

//import com.petter.remembeer.screens.ReceivedBeerScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.screens.AllBeerScreen
import com.petter.remembeer.screens.BeerDetailScreen
import com.petter.remembeer.screens.BeerScreen
import com.petter.remembeer.screens.BeerTypePictureScreen
import com.petter.remembeer.screens.NavigationItem
import java.util.UUID


@Composable
fun Navigations(navController: NavHostController, viewModel: BeerViewModel) {

    NavHost(navController, startDestination = NavigationItem.MyBeer.route) {
        composable(route = NavigationItem.MyBeer.route,
            ) {
            BeerScreen(navController, viewModel)
        }
        /*composable(NavigationItem.ReceivedBeer.route) {
            ReceivedBeerScreen(navController,viewModel)
        }*/
        composable(NavigationItem.AllBeer.route) {
            AllBeerScreen(navController, viewModel)
        }

        composable(route = NavigationItem.BeerDetail.route + "/{beerId}") { backStackEntry ->
            val beerId = UUID.fromString(backStackEntry.arguments?.getString("beerId") ?: "")
            val beerState by viewModel.beerlistobs.collectAsState(initial = emptyList())

            val selectedBeer = beerState.find { it.uid == beerId }

            if (selectedBeer != null) {
                BeerDetailScreen(viewModel, selectedBeer, onDismiss = {})
            } else {
                // Handle case where selected beer is null
                Text("Selected beer not found")
            }
        }

        composable(route = NavigationItem.BeerType.route + "/{beerType}",
        ) { backStackEntry ->
            val beerType = backStackEntry.arguments?.getString("beerType") ?: ""
            val beerState by viewModel.beerlistobs.collectAsState(initial = emptyList())
            val beersOfSelectedType = beerState.filter { it.type == beerType }

            if (beersOfSelectedType.isNotEmpty()) {
                BeerTypePictureScreen(navController, viewModel, beersOfSelectedType, beerType)
            } else {
                // Handle case where no beers of selected type are found
                BeerScreen(navController, viewModel)
            }
        }

        /*composable(NavigationItem.Settings.route) {
            SettingsScreen()
        }*/
    }
}


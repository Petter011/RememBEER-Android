package com.petter.remembeer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.petter.remembeer.helper.Background
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header
import java.util.UUID

@Composable
fun AllBeerScreen(
    navController : NavHostController,
    viewModel: BeerViewModel,
) {

    //val allBeerList by viewModel.allBeerList.collectAsState(initial = mutableListOf())
    val beerlistobs by viewModel.beerlistobs.collectAsState(initial = mutableListOf())
    var showSheet by remember { mutableStateOf(false) }
    var selectedBeerId by remember { mutableStateOf<UUID?>(null) }

    if (showSheet) {
        BottomSheetDetail(onDismiss = { showSheet = false }, viewModel, selectedBeerId)
    }

    Background()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(text = "All Beer")
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .weight(1f)
        ) {
            items(beerlistobs) { beer ->

                //beerList.forEach { beer ->
                    val imageUri = beer.image
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Beer Image",
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { /*navController.navigate("${NavigationItem.BeerDetail.route}/${beer.uid}") */
                                    selectedBeerId = beer.uid; showSheet = true

                                }
                        )
                    }
                }
            }
        }
    }

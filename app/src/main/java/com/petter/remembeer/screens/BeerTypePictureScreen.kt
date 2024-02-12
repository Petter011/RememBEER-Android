package com.petter.remembeer.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.petter.remembeer.helper.Background
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header

@SuppressLint("RememberReturnType")
@Composable
fun BeerTypePictureScreen(
    navController: NavHostController,
    viewModel: BeerViewModel,
    selectedBeer: Beer
) {
    val beersState by viewModel.beers.collectAsState()

    Background()
    Column {
        Header(text = selectedBeer.type)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f)
        ) {
            items(beersState.filter { it.type == "IPA" }) { beer ->
                val imageUri = remember { beer.image }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Display the beer image if available
                        imageUri?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Beer Image",
                                modifier = Modifier
                                    .clickable { navController.navigate("${NavigationItem.BeerDetail.route}/${beer.id}") }
                                    .size(250.dp)
                                    //.aspectRatio(1f), // Keep aspect ratio to avoid distortion
                                //contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }



/*
val imageUri = remember { selectedBeer.image }

    Background()

    if (imageUri != null) {
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "Beer Image",
            modifier = Modifier
                .size(150.dp)
                .clickable { navController.navigate("${NavigationItem.BeerDetail.route}/${selectedBeer.id}") }
        )
    }

 */
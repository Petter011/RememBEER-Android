package com.petter.remembeer.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header
import java.util.UUID


@Composable
fun BeerScreen() {
    val viewModel: BeerViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Header(text = "My Beer")
        ListView(viewModel = viewModel)

    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddBeerButton(text = "Add Beer")

    }
}



@Composable
fun ListView(
    //navController: NavHostController,
    viewModel: BeerViewModel
) {

    val beers = viewModel.beers.value

    Column {
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(beers) { beer ->
                Card(
                    modifier = Modifier
                        .clickable {
                            /*navController.navigate(NavigationItem.BeerDetail.route)
                            {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(NavigationItem.MyBeer.route) {
                                    inclusive = true // Set inclusive to true to remove MyBeer from the back stack as well
                                }
                                Log.d("Get beerId","beerId: {beerId}")

                            }*/
                        }
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    /*colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),*/
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = beer.type,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AddBeerButton(
    text: String,
) {
    val beerViewModel: BeerViewModel = viewModel()

    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        BottomSheet(onDismiss = { showSheet = false }, viewModel = beerViewModel)
    }
    // Button to trigger the bottom sheet
    ElevatedButton(
        onClick = { showSheet = true },
        modifier = Modifier.padding(36.dp),
        colors = ButtonDefaults.buttonColors(Color.Black)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.Yellow
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    viewModel: BeerViewModel
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        AddBeerSheet(
            viewModel = viewModel,
            onBeerAdded = onDismiss
        )
    }
}

@Composable
fun AddBeerSheet(
    viewModel: BeerViewModel,
    onBeerAdded: () -> Unit
) {
    var beerType by remember { mutableStateOf("") }
    var beerName by remember { mutableStateOf("") }
    var beerNote by remember { mutableStateOf("") }
    var beerRating by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .padding(vertical = 50.dp)
    ) {
        OutlinedTextField(
            value = beerType,
            onValueChange = { beerType = it },
            label = { Text(text = "Type") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
        )
        OutlinedTextField(
            value = beerName,
            onValueChange = { beerName = it },
            label = { Text(text = "Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
        )
        OutlinedTextField(
            value = beerNote,
            onValueChange = { beerNote = it },
            label = { Text(text = "Note") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
        )
        OutlinedTextField(
            value = beerRating.toString(),
            onValueChange = { beerRating = it.toIntOrNull() ?: 0 },
            label = { Text(text = "Rating") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
        )
    }
    FloatingActionButton(
        onClick = {
            val beerId = UUID.randomUUID()
            // Create a new Beer object with the entered details
            val newBeer = Beer(
                id = beerId,
                type = beerType,
                name = beerName,
                note = beerNote,
                rating = beerRating
            )
            // Add the beer to the ViewModel
            viewModel.addBeer(newBeer)
            Log.d("AddBeerSheet", "New beer added: $newBeer")

            onBeerAdded()

        },
        modifier = Modifier
            .padding(16.dp)
    ) {
        Icon(Icons.Filled.Add, "Add Beer")
    }
}


/*@Preview(showBackground = true)
@Composable
fun BeerScreenPreview() {
    BeerScreen()
}*/
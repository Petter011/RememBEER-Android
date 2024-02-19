package com.petter.remembeer.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.petter.remembeer.helper.Background
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header


@Composable
fun BeerScreen(
    navController: NavHostController,
    viewModel: BeerViewModel
) {
    Background()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Header(text = "My Beer")
        ListView(viewModel = viewModel, navController = navController)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddBeerButton(text = "Add Beer", viewModel = viewModel, navController = navController)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListView(
    navController: NavHostController,
    viewModel: BeerViewModel
) {
    val beersState by viewModel.beers.collectAsState()

// Preprocess the list to merge consecutive beers with the same type and filter by not scanned
    val mergedNotScannedBeers = mutableListOf<Beer>()
    var previousType: String? = null
    for (beer in beersState) {
        if (beer.isScanned.not() && beer.type != previousType) {
            mergedNotScannedBeers.add(beer)
            previousType = beer.type
        }
    }


    Column {
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f)
        ) {
            items(mergedNotScannedBeers) { beer ->
                Card(
                    modifier = Modifier
                        .clickable {
                            navController.navigate("${NavigationItem.BeerType.route}/${beer.id}")
                            {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(NavigationItem.MyBeer.route) {
                                    inclusive =
                                        false // Set inclusive to true to remove MyBeer from the back stack as well
                                }
                            }
                        }
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = beer.type,
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Yellow,
                                textAlign = TextAlign.Center,
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
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
    viewModel: BeerViewModel,
    text: String,
    navController: NavHostController
) {
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        BottomSheet(onDismiss = { showSheet = false }, viewModel = viewModel, navController)
    }
    ElevatedButton(
        onClick = { showSheet = true },
        modifier = Modifier
            .padding(36.dp)
            .size(width = 230.dp, height = 70.dp),
        colors = ButtonDefaults.buttonColors(Color.Black),

    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Yellow
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    viewModel: BeerViewModel,
    navController: NavHostController
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier
            .padding(top = 20.dp)
    ) {
        AddBeerSheet(
            viewModel = viewModel,
            onBeerAdded = onDismiss,
            navController
        )
    }
}




/*@Composable
fun SampleDefault() {
    FVerticalWheelPicker(
        modifier = Modifier.width(60.dp),
        // Specified item count.
        count = 50,
        debug = true,
    )
}*/




/*@Preview(showBackground = true)
@Composable
fun BeerScreenPreview() {
    BeerScreen()
}*/
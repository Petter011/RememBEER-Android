package com.petter.remembeer.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.petter.remembeer.R
import com.petter.remembeer.helper.Background
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("RememberReturnType")
@Composable
fun BeerTypePictureScreen(
    navController: NavHostController,
    viewModel: BeerViewModel,
    selectedBeer: Beer
) {
    val beersState by viewModel.beers.collectAsState()

    var showSheet by remember { mutableStateOf(false) }

    var selectedBeerId by remember { mutableStateOf<UUID?>(null) }
    val qrCodeBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(selectedBeer.id) {
        val qrBitmap = viewModel.generateQRCodeBitmapForBeer(selectedBeer.id)
        qrCodeBitmap.value = qrBitmap
    }

    if (showSheet) {
        BottomSheetDetail(onDismiss = { showSheet = false }, viewModel, selectedBeerId)
    }

    Background()
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Header(text = selectedBeer.type)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f)
        ) {
            items(beersState.filter { it.type == selectedBeer.type }) { beer ->
                val imageUri = remember { beer.image }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding( 10.dp)
                ) {
                    // Display the beer image if available
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Beer Image",
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(20.dp))
                                //.aspectRatio(1f), // Keep aspect ratio to avoid distortion
                                //contentScale = ContentScale.Crop
                                //.clickable { selectedBeerId = beer.id; showSheet = true }
                                .combinedClickable(onClick = {
                                    selectedBeerId = beer.id; showSheet = true
                                }, onLongClick = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                                },
                                    onLongClickLabel = stringResource(R.string.open_context_menu)
                                )
                        )
                    }
                }
                /*if (selectedBeer != null) {
                    PhotoActionsSheet(
                        photo = selectedBeerId { it.id == selectedBeerId },
                        onDismissSheet = { contextMenuPhotoId = null }
                    )
                }*/
            }
        }
    }
}

@Composable
fun ShowQRCode(){

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDetail(
    onDismiss: () -> Unit,
    viewModel: BeerViewModel,
    beerId: UUID? // Pass beer ID
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.padding(top = 20.dp)
    ) {
        beerId?.let { id ->
            val selectedBeer = viewModel.getBeerById(id)
            selectedBeer?.let {
                BeerDetailScreen(viewModel, it)
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
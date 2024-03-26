package com.petter.remembeer.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.petter.remembeer.helper.Background
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header
import java.util.UUID


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BeerTypePictureScreen(
    navController: NavHostController,
    viewModel: BeerViewModel,
    selectedBeer: List<Beer>,
    beerType: String,
) {

    val beerlistobs by viewModel.beerlistobs.collectAsState(initial = mutableListOf())

    var showSheet by remember { mutableStateOf(false) }

    var selectedBeerId by remember { mutableStateOf<UUID?>(null) }
    val haptics = LocalHapticFeedback.current
    val selectedBeerNames = selectedBeer.map { it.name }.toSet()
    var contextMenuQRId by remember { mutableStateOf(false) }

    if (showSheet) {
        BottomSheetDetail(onDismiss = { showSheet = false }, viewModel, selectedBeerId)
    }
    if (contextMenuQRId) {
        BottomSheetQR( onDismiss = {contextMenuQRId = false}, viewModel, selectedBeerId)
    }

    Background()
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Header(text = beerType)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f)
        ) {
            items(beerlistobs.filter { it.name in selectedBeerNames }) { beer ->
                val imageUri = beer.image
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(10.dp)
                ) {
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Beer Image",
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(20.dp))
                                /*.combinedClickable(
                                    onClick = { selectedBeerId = beer.uid; showSheet = true },
                                    onLongClick = {
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectedBeerId = beer.uid; contextMenuQRId = true
                                    },
                                    //onLongClickLabel = stringResource(R.string.open_context_menu)
                                )*/
                            .clickable(onClick = {
                                        selectedBeerId = beer.uid; showSheet = true
                                    })
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetQR(
    onDismiss: () -> Unit,
    viewModel: BeerViewModel,
    beerId: UUID?,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.padding(top = 20.dp)
    ) {

        beerId?.let {
            val beerState by viewModel.beerlistobs.collectAsState(initial = emptyList())
            val selectedBeer = beerState.find { it.uid == beerId }
            selectedBeer?.let {
                QR(viewModel, it)
            }
        }


    }
}

@Composable
fun QR(
    viewModel: BeerViewModel,
    selectedBeer: Beer,
){
    val qrCodeBitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(selectedBeer.uid) {
        val beerImageBitmap = BitmapFactory.decodeFile(selectedBeer.image)
        val qrBitmap = viewModel.generateQRCodeBitmapForBeer(selectedBeer.uid, beerImageBitmap)
        qrCodeBitmap.value = qrBitmap
    }
    Column {

        qrCodeBitmap.value?.let { qrBitmap ->
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDetail(
    onDismiss: () -> Unit,
    viewModel: BeerViewModel,
    beerId: UUID?,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.padding(top = 20.dp)
    ) {
        beerId?.let {
            val beerState by viewModel.beerlistobs.collectAsState(initial = emptyList())
            val selectedBeer = beerState.find { it.uid == beerId }
            selectedBeer?.let {
                BeerDetailScreen(viewModel, it, onDismiss)
            }
        }
    }
}




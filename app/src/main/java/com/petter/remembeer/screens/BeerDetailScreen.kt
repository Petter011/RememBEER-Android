package com.petter.remembeer.screens

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BeerDetailScreen(
    //navController: NavHostController,
    viewModel: BeerViewModel,
    selectedBeer: Beer,
) {

    val imageUri = remember { selectedBeer.image }
    val context = LocalContext.current
    val qrCodeBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val haptics = LocalHapticFeedback.current


    LaunchedEffect(selectedBeer.id) {
        val qrBitmap = viewModel.generateQRCodeBitmapForBeer(selectedBeer.id)
        qrCodeBitmap.value = qrBitmap
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = selectedBeer.type,
            color = Color.Black,
            fontSize = 24.sp
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Name: ${selectedBeer.name}",
                color = Color.Black,
                fontSize = 24.sp
            )
            Text(
                text = "Note: ${selectedBeer.note}",
                color = Color.Black,
                fontSize = 24.sp
            )
            Text(
                text = "Rating: ${selectedBeer.rating}",
                color = Color.Black,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (imageUri != null) {
                Box(
                    modifier = Modifier
                        .size(150.dp)

                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Beer Image",
                        modifier = Modifier.fillMaxSize()
                    )
                    qrCodeBitmap.value?.let { qrBitmap ->
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}


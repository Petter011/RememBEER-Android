package com.petter.remembeer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.petter.remembeer.helper.Background
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header
import java.io.File


@Composable
fun BeerDetailScreen(
    navController: NavHostController,
    viewModel: BeerViewModel,
    selectedBeer: Beer,
) {

    val imageUri = remember { selectedBeer.image }
    val context = LocalContext.current

    val qrCodeFile = remember {
        mutableStateOf<File?>(null)
    }

    LaunchedEffect(imageUri) {
        if (imageUri != null) {
            // Call the suspend function within the coroutine
            val qrFile = viewModel.generateQRCodeForBeer(selectedBeer.id)
            qrCodeFile.value = qrFile
        }
    }

    Background()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Header(text = selectedBeer.type)
    }
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
            color = Color.White,
            fontSize = 24.sp
        )
        Text(
            text = "Note: ${selectedBeer.note}",
            color = Color.White,
            fontSize = 24.sp
        )
        Text(
            text = "Rating: ${selectedBeer.rating}",
            color = Color.White,
            fontSize = 24.sp
        )

        if (imageUri != null) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clickable {

                    }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Beer Image",
                    modifier = Modifier.fillMaxSize()
                )
                // Show the QR code if it's generated
                qrCodeFile.value?.let { qrFile ->
                    Image(
                        painter = rememberAsyncImagePainter(qrFile),
                        contentDescription = "QR Code",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

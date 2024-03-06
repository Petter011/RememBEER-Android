package com.petter.remembeer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel


@Composable
fun BeerDetailScreen(
    //navController: NavHostController,
    viewModel: BeerViewModel,
    selectedBeer: Beer,
) {

    val imageUri = remember { selectedBeer.image}
    val context = LocalContext.current
    //val qrCodeBitmap = remember { mutableStateOf<Bitmap?>(null) }


    /*LaunchedEffect(selectedBeer.id) {
        val beerImageBitmap = BitmapFactory.decodeFile(selectedBeer.image)
        val qrBitmap = viewModel.generateQRCodeBitmapForBeer(selectedBeer.id, beerImageBitmap)
        qrCodeBitmap.value = qrBitmap
    }*/
    Row(
        horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(end = 10.dp)
    ){
        IconButton(
            onClick = {
                viewModel.deleteBeer(selectedBeer)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(1f),
            //.padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = selectedBeer.type!!,
            color = Color.Black,
            fontSize = 24.sp
        )
        Column(
            modifier = Modifier
                .fillMaxSize(1f)
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

            if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Beer Image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .fillMaxSize(1f)
                    )
                    /*qrCodeBitmap.value?.let { qrBitmap ->
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.fillMaxSize()
                        )
                    }*/
            }
        }
    }
}


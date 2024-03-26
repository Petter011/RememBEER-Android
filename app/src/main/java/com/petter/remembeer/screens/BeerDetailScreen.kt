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
    onDismiss: () -> Unit,
    ) {

    val imageUri = remember { selectedBeer.image}

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
                .fillMaxWidth()
        ){
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = selectedBeer.type!!,
            color = Color.Black,
            fontSize = 24.sp,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp)
        )

        IconButton(
            onClick = {
                viewModel.deleteBeer(selectedBeer)
                onDismiss()
            },
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

        Column(
            modifier = Modifier
                .fillMaxSize(1f),
                //.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Name: ${selectedBeer.name}",
                color = Color.Black,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(5.dp)
            )
            Text(
                text = "Note: ${selectedBeer.note}",
                color = Color.Black,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(5.dp)
            )
            Text(
                text = "Rating: ${selectedBeer.rating}",
                color = Color.Black,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(5.dp)
            )

            if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Beer Image",
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .padding(all = 10.dp)
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


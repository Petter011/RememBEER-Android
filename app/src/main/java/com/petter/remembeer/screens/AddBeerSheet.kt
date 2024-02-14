package com.petter.remembeer.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.petter.remembeer.camera.ComposeFileProvider
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel

@Composable
fun AddBeerSheet(
    viewModel: BeerViewModel,
    onBeerAdded: () -> Unit,
    navController: NavHostController,
) {
    var beerType by remember { mutableStateOf("") }
    var beerName by remember { mutableStateOf("") }
    var beerNote by remember { mutableStateOf("") }
    var beerRating by remember { mutableStateOf(0) }

    var hasImage by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Add Beer",
            style = TextStyle(
                fontSize = 24.sp
            )
        )
    }
    Column(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxSize(1f)
    ) {
        OutlinedTextField(
            value = beerType,
            onValueChange = { beerType = it },
            label = { Text(text = "Beer Type") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = beerName,
            onValueChange = { beerName = it },
            label = { Text(text = "Beer Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = beerNote,
            onValueChange = { beerNote = it },
            label = { Text(text = "Add a Note") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = beerRating.toString(),
            onValueChange = { beerRating = it.toIntOrNull() ?: 0 },
            label = { Text(text = "Rating") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            ElevatedButton(
                onClick = {
                    val uri = ComposeFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                },
                modifier = Modifier
                    .size(width = 150.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text(text = "Take a picture")
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(120.dp)
            ) {
                // Show image here
                if (hasImage) {
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .fillMaxSize()
                                //.height(150.dp)
                                .padding(vertical = 16.dp)
                        )
                    }
                }
            }
            ElevatedButton(
                onClick = {
                    // Create a new Beer object with the entered details
                    val newBeer = Beer(
                        type = beerType,
                        name = beerName,
                        note = beerNote,
                        rating = beerRating,
                        image = imageUri?.toString()
                    )
                    // Add the beer to the ViewModel
                    viewModel.addBeer(newBeer, imageUri)
                    Log.d("AddBeerSheet", "New beer added: $newBeer")

                    onBeerAdded()
                },
                modifier = Modifier
                    .size(width = 150.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(Color.Black),
            ) {
                Text(
                    text = "Save",
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}
//viewModel.addBeer(newBeer, imageFile = imageUri?.let { File(it.toString()) })

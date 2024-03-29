package com.petter.remembeer.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.andyliu.compose_wheel_picker.VerticalWheelPicker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.petter.remembeer.camera.ComposeFileProvider
import com.petter.remembeer.helper.BeerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
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

    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )
    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

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
        horizontalAlignment = Alignment.CenterHorizontally,
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
        Spacer(modifier = Modifier.height(10.dp))
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
        Spacer(modifier = Modifier.height(10.dp))
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
        Spacer(modifier = Modifier.height(10.dp))
        NumberPicker(onValueChanged = { selectedValue ->
            beerRating = selectedValue
        })

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            ElevatedButton(
                onClick = {
                    if (!cameraPermissionState.status.isGranted){
                        cameraPermissionState.launchPermissionRequest()
                    } else {
                        val uri = ComposeFileProvider.getImageUri(context)
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    }
                },
                modifier = Modifier
                    .size(width = 150.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text(text = "Take a picture", color = Color.Yellow)

            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(120.dp)
            ) {
                if (hasImage) {
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                //.fillMaxSize()
                                .size(130.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .padding(vertical = 16.dp)
                        )
                    }
                }
            }
            ElevatedButton(
                onClick = {
                    if (beerType.isEmpty() || imageUri == null) {
                        showDialog = true
                    } else {
                        imageUri?.toString()?.let { viewModel.addBeer(
                            beerType,
                            beerName,
                            beerNote,
                            beerRating,
                            beerImage = it) }
                        onBeerAdded()
                    }
                },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .size(width = 150.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(Color.Black),
            ) {
                Text(
                    text = "Save",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Yellow
                    )
                )
            }
            // Alert dialog for empty beerType or null imageUri
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false }, // Dismiss the dialog
                    title = { Text(text = "Error") },
                    text = {
                        Text(text = "Please enter beer type and take an picture.")
                    },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false } // Dismiss the dialog
                        ) {
                            Text(text = "OK")
                        }
                    }
                )
            }
        }
    }
}

@Composable
internal fun NumberPicker(onValueChanged: (Int) -> Unit) {
    val startIndex = 5
    val state = rememberLazyListState(startIndex)
    val scope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(startIndex) }
    VerticalWheelPicker(
        state = state,
        count = 11,
        itemHeight = 24.dp,
        visibleItemCount = 5,
        onScrollFinish = { currentIndex = it }) { index ->
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clickable { scope.launch { state.animateScrollToItem(index) } },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$index",
                color = if (index == currentIndex) Color.Black else Color.Gray
            )
        }
    }
    DisposableEffect(currentIndex) {
        onValueChanged(currentIndex)
        onDispose { }
    }
}

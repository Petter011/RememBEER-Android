package com.petter.remembeer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
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
import com.petter.remembeer.helper.Background
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header

@Composable
fun ReceivedBeerScreen(viewModel: BeerViewModel) {
    Background()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(text = "Received Beer")
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScanBeerButton(viewModel, text = "Scan QR")
    }
}

@Composable
fun ScanBeerButton(
    viewModel: BeerViewModel,
    text: String,

) {
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
       // BottomSheet(onDismiss = { showSheet = false }, viewModel = viewModel)
    }
    ElevatedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(36.dp)
            .size(width = 230.dp, height = 70.dp),
    colors = ButtonDefaults.buttonColors(Color.Black)
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



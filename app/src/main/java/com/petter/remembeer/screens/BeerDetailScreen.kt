package com.petter.remembeer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel

@Composable
fun BeerDetailScreen(
    /*navController: NavController,
    viewModel: BeerViewModel,
    selectedBeer: Beer*/
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Beer Details")
        Spacer(modifier = Modifier.height(16.dp))
        /*Text(text = "Type: ${selectedBeer.type}")
        Text(text = "Name: ${selectedBeer.name}")
        Text(text = "Note: ${selectedBeer.note}")
        Text(text = "Rating: ${selectedBeer.rating}")*/
    }
}
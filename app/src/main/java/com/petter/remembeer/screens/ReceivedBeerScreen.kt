package com.petter.remembeer.screens

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.petter.remembeer.helper.Background
import com.petter.remembeer.helper.Beer
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header
import com.petter.remembeer.helper.parseJsonFromString


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReceivedBeerScreen(navController: NavHostController, viewModel: BeerViewModel
) {
    val scannedResult = remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannedText = result.data?.getStringExtra("SCAN_RESULT")
            scannedResult.value = scannedText
        }
    }

    Background()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(text = "Received Beer")
        ScannedBeerListView(navController, viewModel)

    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Column(
            )
             {
                scannedResult.value?.let { jsonContent ->
                    DisplayBeerInfo(jsonContent, viewModel, navController) // Pass the scanned JSON content to the DisplayBeerInfo function
                }
            }
        ElevatedButton(
            onClick = {
                val intent = Intent(context, ScanActivity::class.java)
                launcher.launch(intent)
                      },
            modifier = Modifier
                .padding(36.dp)
                .size(width = 230.dp, height = 70.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text(
                text = "Scan QR Code",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Yellow
                )
            )
        }
    }
}

@Composable
fun ScannedBeerListView(
    navController: NavHostController,
    viewModel: BeerViewModel
) {
    val scannedBeersState by viewModel.beers.collectAsState()

    val mergedScannedBeers = mutableListOf<Beer>()
    var previousType: String? = null
    for (beer in scannedBeersState) {
        if (beer.isScanned && beer.type != previousType) {
            mergedScannedBeers.add(beer)
            previousType = beer.type
        }
    }

    Column {
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f)
        ) {
            items(mergedScannedBeers) { beer ->
                Card(
                    modifier = Modifier
                        .clickable {
                            navController.navigate("${NavigationItem.BeerType.route}/${beer.id}")
                            {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(NavigationItem.ReceivedBeer.route) {
                                    inclusive = false
                                }
                            }
                        }
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = beer.type,
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Yellow,
                                textAlign = TextAlign.Center,
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}



@Composable
fun DisplayBeerInfo(jsonString: String, viewModel: BeerViewModel, navController: NavController) {
    val scannedBeer = parseJsonFromString(jsonString)
val imageUri = null
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.LightGray)
    ) {
        if (scannedBeer != null) {
            Text(
                text = "Beer Name: ${scannedBeer.type}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Type: ${scannedBeer.name}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Note: ${scannedBeer.note}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Rating: ${scannedBeer.rating}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        } else {
            Text(
                text = "Error parsing beer information",
                textAlign = TextAlign.Center
            )
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(bottom = 20.dp)
        ) {
            ElevatedButton(onClick = {
               if (scannedBeer != null) {
                    viewModel.addBeer(scannedBeer, imageUri, isScanned = true)
                   Log.d("ScannedBeerSheet", "Scanned beer added: $scannedBeer")
                   navController.navigate(route = NavigationItem.ReceivedBeer.route)
               }
            }) {
                Text(text = "Save")
            }
        }

    }
}

class ScanActivity : AppCompatActivity() {
    private lateinit var barcodeView: DecoratedBarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcodeView = DecoratedBarcodeView(this)
        setContentView(barcodeView)

        barcodeView.decodeContinuous(callback)
    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            result?.let {
                val scannedText = result.text
                if (!scannedText.isNullOrEmpty()) {
                    val intent = Intent().apply {
                        putExtra("SCAN_RESULT", scannedText) // Pass scanned text directly as result data
                    }
                    setResult(Activity.RESULT_OK, intent)
                }
            }
            finish()
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}


